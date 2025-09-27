package com.calyrsoft.ucbp1.features.movies.data.repository

import com.calyrsoft.ucbp1.features.movies.data.api.dto.MovieDto
import com.calyrsoft.ucbp1.features.movies.data.datasource.MovieLocalDataSource

import com.calyrsoft.ucbp1.features.movies.data.datasource.remote.MovieRemoteDataSource
import com.calyrsoft.ucbp1.features.movies.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.movies.domain.model.Movie
import com.calyrsoft.ucbp1.features.movies.domain.repository.MovieRepository
class MovieRepositoryImpl(
    private val remote: MovieRemoteDataSource,
    private val local: MovieLocalDataSource
) : MovieRepository {

    override suspend fun getPopularMovies(): Result<List<Movie>> {
        val remoteResult = remote.getPopularMovies().map { dtos ->
            // Traer lo que ya existe en la base local
            val localMovies = local.getAll()

            dtos.map { dto ->
                // Si ya estaba guardado en local, conserva su "isLiked"
                val existing = localMovies.find { it.id == dto.id }
                dto.toDomain().copy(isLiked = existing?.isLiked ?: false)
            }
        }

        if (remoteResult.isSuccess) {
            // Guardar en Room con los likes conservados
            local.upsertAll(remoteResult.getOrNull().orEmpty())
            // Devolver desde la base local (ya ordenado por isLiked)
            return runCatching { local.getAll() }
        }

        // fallback a cache local si falla la red
        return runCatching { local.getAll() }
    }



    override suspend fun toggleLike(movieId: Int) {
        local.toggleLike(movieId)
    }
    private fun MovieDto.toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            isLiked = false
        )
    }

}

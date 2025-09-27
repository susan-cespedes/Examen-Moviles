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
            dtos.map { it.toDomain() }
        }

        if (remoteResult.isSuccess) {
            // guardar/actualizar en Room
            local.upsertAll(remoteResult.getOrNull().orEmpty())
            return remoteResult
        }

        // fallback a cache local si hay error de red
        return runCatching { local.getAll() }
    }
}

private fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

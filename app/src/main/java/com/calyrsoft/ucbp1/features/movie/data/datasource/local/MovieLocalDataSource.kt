package com.calyrsoft.ucbp1.features.movies.data.datasource
import com.calyrsoft.ucbp1.features.movies.data.database.dao.IMovieDao
import com.calyrsoft.ucbp1.features.movies.data.mapper.toDomain
import com.calyrsoft.ucbp1.features.movies.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.movies.domain.model.Movie

class MovieLocalDataSource(
    private val dao: IMovieDao
) {
    suspend fun getAll(): List<Movie> {
        return dao.getAll().map { it.toDomain() }
    }

    suspend fun upsertAll(movies: List<Movie>) {
        dao.upsertAll(movies.map { it.toEntity() })
    }

    suspend fun clear() {
        dao.clearAll()
    }
    suspend fun toggleLike(movieId: Int) {
        dao.toggleLike(movieId)
    }
}

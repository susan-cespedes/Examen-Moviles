package com.calyrsoft.ucbp1.features.movies.data.mapper

import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieEntity
import com.calyrsoft.ucbp1.features.movies.domain.model.Movie
fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,               // String → String? (ok)
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,         // String → String (ok)
        voteAverage = voteAverage,         // Double → Double? (ok)
        voteCount = voteCount              // Int → Int? (ok)
    )
}

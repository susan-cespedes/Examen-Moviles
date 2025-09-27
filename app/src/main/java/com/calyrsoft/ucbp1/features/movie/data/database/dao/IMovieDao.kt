package com.calyrsoft.ucbp1.features.movies.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieEntity

@Dao
interface IMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    suspend fun getAll(): List<MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}

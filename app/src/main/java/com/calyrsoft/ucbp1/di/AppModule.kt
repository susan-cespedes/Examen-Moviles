package com.calyrsoft.ucbp1.di

import com.calyrsoft.ucbp1.features.dollar.data.database.AppRoomDatabase
import com.calyrsoft.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.data.repository.DollarRepositoryImpl
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.FetchDollarUseCase
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarHistoryViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarViewModel

import com.calyrsoft.ucbp1.features.movies.data.api.MovieService
import com.calyrsoft.ucbp1.features.movies.data.database.MoviesRoomDatabase
import com.calyrsoft.ucbp1.features.movies.data.datasource.MovieLocalDataSource
import com.calyrsoft.ucbp1.features.movies.data.datasource.remote.MovieRemoteDataSource
import com.calyrsoft.ucbp1.features.movies.data.repository.MovieRepositoryImpl
import com.calyrsoft.ucbp1.features.movies.domain.repository.MovieRepository
import com.calyrsoft.ucbp1.features.movies.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.features.movies.presentation.viewmodel.MoviesViewModel

import com.calyrsoft.ucbp1.features.profile.data.ProfileRepositoryImpl
import com.calyrsoft.ucbp1.features.profile.domain.repository.ProfileRepository
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileViewModel

import com.google.firebase.database.FirebaseDatabase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    // --- Network (OkHttp + Retrofit) ---
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/") // TMDB base
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<MovieService> {
        get<Retrofit>().create(MovieService::class.java)
    }

    // --- Firebase ---
    single { FirebaseDatabase.getInstance() }

    // --- Databases (crear BD antes de pedir DAOs) ---
    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }

    single { MoviesRoomDatabase.getDatabase(get()) }
    single { get<MoviesRoomDatabase>().movieDao() }

    // --- DataSources ---
    single { RealTimeRemoteDataSource(get()) }
    single { DollarLocalDataSource(get()) }

    single { MovieRemoteDataSource(get()) }           // usa MovieService
    single { MovieLocalDataSource(get()) }            // usa MovieDao

    // --- Repositories ---
    single<IDollarRepository> { DollarRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl() }

    single<MovieRepository> { MovieRepositoryImpl(remote = get(), local = get()) }

    // --- UseCases ---
    factory { FetchDollarUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { GetPopularMoviesUseCase(get()) }

    // --- ViewModels ---
    viewModel { ProfileViewModel(get()) }
    viewModel { DollarViewModel(get(), get()) }
    viewModel { MoviesViewModel(get(),get()) }
    viewModel { DollarHistoryViewModel(get()) }
}
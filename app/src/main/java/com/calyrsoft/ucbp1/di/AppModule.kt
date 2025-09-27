package com.calyrsoft.ucbp1.di

import NotificationViewModel

import com.calyrsoft.ucbp1.core.AuthManager
import com.calyrsoft.ucbp1.features.dollar.data.database.AppRoomDatabase
import com.calyrsoft.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.data.repository.DollarRepositoryImpl
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.FetchDollarUseCase
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarHistoryViewModel
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarViewModel
import com.google.firebase.database.FirebaseDatabase
import com.calyrsoft.ucbp1.features.movies.data.api.MovieService
import com.calyrsoft.ucbp1.features.movies.data.database.MoviesRoomDatabase
import com.calyrsoft.ucbp1.features.movies.data.datasource.remote.MovieRemoteDataSource
import com.calyrsoft.ucbp1.features.movies.data.repository.MovieRepositoryImpl
import com.calyrsoft.ucbp1.features.movies.domain.repository.MovieRepository
import com.calyrsoft.ucbp1.features.movies.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.features.movies.presentation.viewmodel.MoviesViewModel
import com.calyrsoft.ucbp1.features.notification.data.repository.NotificationRepository
import com.calyrsoft.ucbp1.features.notification.data.repository.NotificationRepositoryImpl
import com.calyrsoft.ucbp1.features.profile.data.ProfileRepositoryImpl
import com.calyrsoft.ucbp1.features.profile.domain.repository.ProfileRepository
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { AuthManager(get()) }

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }




    // MovieDB Service (base URL diferente)
    single<MovieService> {
        get<Retrofit>().newBuilder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()) // 👈 necesario
            .build()
            .create(MovieService::class.java)
    }

    // Firebase Realtime Database
    single { FirebaseDatabase.getInstance() }


    // DataSources
    single { RealTimeRemoteDataSource(get()) }
    single { DollarLocalDataSource(get()) }
    single { MovieRemoteDataSource(get()) }

    single { com.calyrsoft.ucbp1.features.movies.data.datasource.MovieLocalDataSource(get()) }

    // Database
    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { get<MoviesRoomDatabase>().movieDao() }
    single { MoviesRoomDatabase.getDatabase(get()) }


    // Repositories
    single<IDollarRepository> { DollarRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl() }
    single<MovieRepository> { MovieRepositoryImpl(get(),get()) }
    single { NotificationRepositoryImpl() as NotificationRepository }

    // UseCases
    factory { FetchDollarUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { GetPopularMoviesUseCase(get()) }

    // ViewModels
    viewModel { ProfileViewModel(get()) }
    viewModel { DollarViewModel(get(), get()) }
    viewModel { MoviesViewModel(get()) }
    viewModel { DollarHistoryViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
}

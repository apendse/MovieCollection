package com.aap.ro.movies.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This is the module that provides a real movie repository.
 * It uses the instance of [MovieRepositoryImpl] to provide a [MovieRepository] instance
 * It's installed a singleton module. The bindRepository method is marked with Singleton and Binds.
 * Binds indicates that this binds to an instance of [MovieRepository] and Singleton indicates
 * that it reuses the instance. Not create a new one every time someone requests one.
 */
@Module
@InstallIn(SingletonComponent::class)
interface MovieRepositoryModule {
    @Singleton
    @Binds
    fun bindToRepository(movieRepository: MovieRepositoryImpl): MovieRepository
}
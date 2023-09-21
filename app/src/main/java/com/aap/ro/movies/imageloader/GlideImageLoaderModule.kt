package com.aap.ro.movies.imageloader

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GlideImageLoaderModule {
    @Singleton
    @Binds
    fun provideImageLoader(imageLoader: GlideImageLoader): Imageloader
}
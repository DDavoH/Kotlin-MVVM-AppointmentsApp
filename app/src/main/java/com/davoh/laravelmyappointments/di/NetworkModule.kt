package com.davoh.laravelmyappointments.di

import com.davoh.laravelmyappointments.api.LaravelApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideLaravelService(): LaravelApiService {
        return LaravelApiService.create()
    }

}
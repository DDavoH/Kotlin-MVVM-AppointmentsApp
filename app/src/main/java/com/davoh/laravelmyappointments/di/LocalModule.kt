package com.davoh.laravelmyappointments.di

import android.content.Context
import androidx.room.Room
import com.davoh.laravelmyappointments.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Singleton
    @Provides
    fun provideRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "db_myAppointments"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideAppointmentDao(db: AppDatabase) = db.appointmentDao()
}
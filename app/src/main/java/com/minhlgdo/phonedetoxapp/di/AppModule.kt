package com.minhlgdo.phonedetoxapp.di

import android.content.Context
import com.minhlgdo.phonedetoxapp.data.local.AppDatabase
import com.minhlgdo.phonedetoxapp.data.local.ServiceManager
import com.minhlgdo.phonedetoxapp.data.repository.JournalRepository
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import com.minhlgdo.phonedetoxapp.data.repository.UsageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
   @Provides
   @Singleton
   fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
      return AppDatabase.getInstance(context)
   }

    @Provides
    @Singleton
    fun provideAppRepository(db: AppDatabase) : PhoneAppRepository {
        return PhoneAppRepository(db.appDao())
    }

    @Provides
    @Singleton
    fun provideJournalRepository(db: AppDatabase) : JournalRepository {
        return JournalRepository(db.journalDao())
    }

    @Provides
    @Singleton
    fun provideUsageRepository(db: AppDatabase) : UsageRepository {
        return UsageRepository(db.usageDao(), db.reasonDao())
    }

    @Provides
    @Singleton
    fun provideServiceManager(@ApplicationContext context: Context) : ServiceManager {
        return ServiceManager(context)
    }



}
package com.minhlgdo.phonedetoxapp.di

import android.content.Context
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppDatabase
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
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
   fun provideAppDatabase(@ApplicationContext context: Context): PhoneAppDatabase {
      return PhoneAppDatabase.getInstance(context)
   }

    @Provides
    @Singleton
    fun provideAppRepository(db: PhoneAppDatabase) : PhoneAppRepository {
        return PhoneAppRepository(db.dao)
    }



}
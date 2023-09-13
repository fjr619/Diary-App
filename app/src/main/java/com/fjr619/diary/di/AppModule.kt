package com.fjr619.diary.di

import com.fjr619.diary.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providemongoDbApp():App = App.create(Constants.APP_ID)
}
package com.example.rickmorty.di


import android.app.Application
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ViewModelFactoryModule {

    @Provides
    fun provideViewModelFactory(application: Application): ViewModelProvider.Factory {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

}

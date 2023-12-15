package com.example.rickmorty.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.rickmorty.data.characters.local.RickAndMortyDatabase
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
    fun provideAppContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideRickAndMortyDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        RickAndMortyDatabase::class.java,
        "rick_and_morty_db"
    ).build()

    @Singleton
    @Provides
    fun provideCharactersDao(db: RickAndMortyDatabase) = db.characterDao()
}

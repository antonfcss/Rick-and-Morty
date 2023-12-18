package com.example.rickmorty.di


import com.example.rickmorty.base.BaseSource
import com.example.rickmorty.base.BaseSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BaseSourceModule {

    @Binds
    fun bindsBaseSource(source: BaseSourceImpl): BaseSource

}

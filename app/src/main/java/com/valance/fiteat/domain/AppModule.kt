package com.valance.fiteat.domain

import com.valance.fiteat.db.repository.AppRepositoryImpl
import com.valance.fiteat.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository
}
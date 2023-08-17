package com.cookandroid.bookdarak_1.di

import com.cookandroid.bookdarak_1.data.repository.BookSearchRepository
import com.cookandroid.bookdarak_1.data.repository.BookSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBookSearchRepository(
        bookSearchRepositoryImpl: BookSearchRepositoryImpl,
    ): BookSearchRepository
}
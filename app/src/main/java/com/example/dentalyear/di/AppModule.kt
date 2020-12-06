package com.example.dentalyear.di

import android.content.Context
import com.example.dentalyear.data.api.ApiService
import com.example.dentalyear.data.api.DentalApi
import com.example.dentalyear.data.database.ApplicationDatabase
import com.example.dentalyear.data.database.HomeDao
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Utility.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        ApplicationDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideDao(database: ApplicationDatabase) = database.homeDao()

    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    fun provideDentalApi(apiService: ApiService) = DentalApi(apiService)

    @Singleton
    @Provides
    fun provideRepository(dentalApi: DentalApi, homeDao: HomeDao) =
        MainRepository(dentalApi, homeDao)


}

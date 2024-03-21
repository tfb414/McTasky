package com.example.mctasky.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.81:3333/" // Your API base URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val taskService: TaskService = retrofit.create(TaskService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Configure your OkHttpClient
        return OkHttpClient.Builder().build()
    }


    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService {
        return retrofit.create(TaskService::class.java)
    }

    @Provides
    @Singleton  // Make sure Retrofit instance lives through Application's lifecycle
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // Add OkHttpClient as needed
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Use your OkHttpClient if you need customization
            .build()
    }
}
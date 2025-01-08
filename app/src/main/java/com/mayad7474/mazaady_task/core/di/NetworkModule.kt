package com.mayad7474.mazaady_task.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mayad7474.mazaady_task.BuildConfig
import com.mayad7474.mazaady_task.data.dataSource.CategoriesAS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCache(): Cache =
        Cache(File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()), 1024)


    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.BASIC
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache, inspector: HttpLoggingInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .cache(cache)
            .retryOnConnectionFailure(true)
            .connectionPool(
                ConnectionPool(30, 500000, TimeUnit.MILLISECONDS)
            )
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(inspector)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(BuildConfig.API_KEY_HEADER, BuildConfig.API_KEY_VALUE)
                    .build()
                chain.proceed(request)
            }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideBaseRetrofit(
        okHttpClient: OkHttpClient.Builder, gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideCategoriesAS(retrofit: Retrofit): CategoriesAS =
        retrofit.create(CategoriesAS::class.java)
}
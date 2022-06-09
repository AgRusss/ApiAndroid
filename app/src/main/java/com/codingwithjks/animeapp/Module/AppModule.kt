package com.codingwithjks.animeapp.Module


import com.codingwithjks.animeapp.Network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    //корневая ссылка на апи
    fun getBaseUrl():String = "https://api.jikan.moe/v4/"

    @Provides
    @Singleton
    fun getRetrofitBuilder(baseUrl:String):Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit):ApiService = retrofit.create(ApiService::class.java)

}
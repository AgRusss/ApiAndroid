package com.codingwithjks.animeapp.Network

import com.codingwithjks.animeapp.Model.AnimeData
import retrofit2.http.GET

interface ApiService {
    //посылка гет запроса топ аниме
    //через retrofit
    @GET("top/anime")
    suspend fun getTopAnime(
    ):AnimeData
}
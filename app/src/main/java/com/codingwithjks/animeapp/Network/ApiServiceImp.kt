package com.codingwithjks.animeapp.Network

import com.codingwithjks.animeapp.Model.AnimeData
import javax.inject.Inject

class ApiServiceImp @Inject constructor(private val apiService: ApiService) {
    //реализация (интерфейса) получения топ аниме из API
    suspend fun getTopAnime():AnimeData = apiService.getTopAnime()
}
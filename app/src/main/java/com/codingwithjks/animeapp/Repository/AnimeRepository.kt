package com.codingwithjks.animeapp.Repository

import com.codingwithjks.animeapp.Model.AnimeData
import com.codingwithjks.animeapp.Network.ApiServiceImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AnimeRepository @Inject constructor(private val apiServiceImp: ApiServiceImp) {
    fun getTopAnime():Flow<AnimeData> = flow {
        val response = apiServiceImp.getTopAnime()
        emit(response)
    }.flowOn(Dispatchers.IO)
        .conflate()
}
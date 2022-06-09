package com.codingwithjks.animeapp.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithjks.animeapp.Model.AnimeData
import com.codingwithjks.animeapp.Repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//класс в котором получаем данные
//корутины нужны для асинхронной работы
//мы не ждем пока запрос придет
@HiltViewModel
class AnimeViewModel @Inject constructor(private val animeRepository: AnimeRepository): ViewModel() {
    val animeTopResponse:MutableLiveData<AnimeData> = MutableLiveData()
    @ExperimentalCoroutinesApi
    private val searchChannel = ConflatedBroadcastChannel<String>()

    //функция отправки запроса
    @ExperimentalCoroutinesApi
    fun setSearchQuery(search:String)
    {
        searchChannel.offer(search)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getTopAnime()
    {
        viewModelScope.launch {
            searchChannel.asFlow() //соз дание потока из массива
                .flatMapLatest { search->    //будет брать последний элемент в потоке и возвращать наш ответ от api
                    animeRepository.getTopAnime()
                }.catch {e->
                    Log.d("main", "${e.message}")
                }.collect { response->                        //собираем данные
                    animeTopResponse.value=response           //и записываем в поле animetopresponse.value
                }
        }
    }
}
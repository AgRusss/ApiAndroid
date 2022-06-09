package com.codingwithjks.animeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjks.animeapp.Model.Anime
import com.codingwithjks.animeapp.ViewModel.AnimeViewModel
import com.codingwithjks.animeapp.ViewModel.RecyclerAdapter
import com.codingwithjks.animeapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val animeViewModel:AnimeViewModel by viewModels()
    var animeListMain: MutableList<Anime> = emptyList<Anime>().toMutableList()
    lateinit var adapter: RecyclerAdapter

    @FlowPreview
    @ExperimentalCoroutinesApi
    //когда приложение запускается
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //кнопка смены активности
        //переход на сохраненные
        binding.btnToStore.setOnClickListener {
            //вызов StoreActivity
            val intent = Intent(this@MainActivity, StoreActivity::class.java)
            startActivity(intent)
        }
        //получение топ аниме и отправка запроса
        animeViewModel.getTopAnime()
        animeViewModel.setSearchQuery("")
    }
    //после старта обновляем список
    override fun onResume() {
        super.onResume()
        updateRecycler()
        print("ON RESUME")
    }

    fun updateRecycler() {
        //получаем сохраненную строку
        var store = getSharedPreferences("anime", Context.MODE_PRIVATE)
        var animeString = store.getString("anime", "{}")
        //создаем объекты
        val myClass: List<Anime> = Gson().fromJson(animeString , Array<Anime>::class.java).toList()
        //когда сервер ответил
        animeViewModel.animeTopResponse.observe(this, Observer { response->
            val listAnime: List<Anime>
            if (myClass.isNotEmpty()) {
                //сравниваем с сохраненными данными
                listAnime = response.data.map (fun(anime): Anime {
                    for (element in myClass) {
                        //помечаем если сохранены
                        if (anime.title == element.title) {
                            anime.isSaved = true
                            break
                        } else {
                            anime.isSaved = false
                        }
                    }
                    return anime
                })
            } else  {
                //если сохраненных нет то оставляем как есть
                listAnime = response.data
            }
            //заполняем recycler view
            val layoutManager = LinearLayoutManager(this)
            binding.listAnime.layoutManager = layoutManager
            //создаем адаптер и заполняем данными
            adapter = RecyclerAdapter()
            adapter.animeList = listAnime.toMutableList()
            animeListMain = listAnime.toMutableList()
            adapter.context = this

            //прикрепляем адаптер к списку
            binding.listAnime.adapter = adapter
        })
    }



}

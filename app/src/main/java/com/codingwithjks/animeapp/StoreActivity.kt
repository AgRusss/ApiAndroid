package com.codingwithjks.animeapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjks.animeapp.Model.Anime
import com.codingwithjks.animeapp.ViewModel.RecyclerAdapter
import com.codingwithjks.animeapp.databinding.ActivityStoreBinding
import com.google.gson.Gson


class StoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreBinding
    //на создании окна
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    //полсе создания
    override fun onResume() {
        super.onResume()
        //получаем сохраненные элементы
        var store = getSharedPreferences("anime", Context.MODE_PRIVATE)
        var animeString = store.getString("anime", "{}")
        //превращаем строку в объекты
        val myClass: List<Anime> = Gson().fromJson(animeString , Array<Anime>::class.java).toList()

        //снова создаем адаптер и назначаем списку
        val adapter = RecyclerAdapter()
        adapter.animeList = myClass.toMutableList()
        adapter.context = this

        val layoutManager = LinearLayoutManager(this)
        binding.savedList.layoutManager = layoutManager
        binding.savedList.adapter = adapter
    }
    //на нажатии клавиши назад. обновляем главный список
    override fun onBackPressed() {
        println("TO MAIN")

        val main = parent as MainActivity
        main.updateRecycler()
        super.onBackPressed()
    }
}








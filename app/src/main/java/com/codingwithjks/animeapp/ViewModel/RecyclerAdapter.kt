package com.codingwithjks.animeapp.ViewModel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingwithjks.animeapp.Model.Anime
import com.codingwithjks.animeapp.R
import com.codingwithjks.animeapp.StoreActivity
import com.google.gson.Gson

//связка отрисовки списка (карточки)
//с данными
class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    //с какой активности вызван
    lateinit var context: Context
    var animeList: MutableList<Anime> = emptyList<Anime>().toMutableList()
    //получение количичества элементов в списке
    override fun getItemCount(): Int {
        return animeList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        //берем шаблон аниме кард
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.anime_card, viewGroup, false)
        return ViewHolder(v)
    }
    //когда ставим данные
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //данные карточки
        viewHolder.itemName.text = animeList[i].title
        viewHolder.itemYear.text = animeList[i].year.toString()
        viewHolder.itemDuration.text = animeList[i].duration
        viewHolder.itemSynopsis.text = animeList[i].synopsis
        //текст кнопки
        if (animeList[i].isSaved) {
            viewHolder.btnSav.text = "Удалить"
        } else {
            viewHolder.btnSav.text = "Сохранить"
        }
        //нажатие на кнопку сохранеия
        viewHolder.btnSav.setOnClickListener{
            //получение SharedPreference
            //внутреннего хранилище
            var store = this.context.getSharedPreferences("anime", Context.MODE_PRIVATE)
            //если нажали на кнопку в сохраненных
            if (context is StoreActivity) {
                println("REMOVE")
                animeList[i].isSaved = false
                //получаем ранее сохраненные из JSON
                var animeString = store.getString("anime", "{}")
                val myClass: List<Anime> = Gson().fromJson(animeString , Array<Anime>::class.java).toList()
                //полчаем все сохраненные и не удаленное (по названию)
                val saveAnimeList: List<Anime> = myClass.filter { anime -> anime.isSaved && anime.title != animeList[i].title }
                //охраняем изменения в SharedPreference
                var editor = store.edit()
                editor.putString("anime",Gson().toJson(saveAnimeList))
                editor.commit()
                //удаляем из списка
                animeList.removeAt(i)
                //удалить именно из списка
                notifyItemRemoved(i)
            } else {
                //нажали из главной
                animeList[i].isSaved = !animeList[i].isSaved
                //меняем текст кнопки
                if (animeList[i].isSaved) {
                    viewHolder.btnSav.text = "Удалить"
                } else {
                    viewHolder.btnSav.text = "Сохранить"
                }
                //берем все которые сохранные
                var savedList =  animeList.filter { anime -> anime.isSaved }
                //сохраняем в sharedPreference
                var editor = store.edit()
                editor.putString("anime",Gson().toJson(savedList))
                editor.commit()
            }
        }
    }
    //описание полей карточки
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemName: TextView
        var itemYear: TextView
        var itemDuration: TextView
        var itemSynopsis: TextView
        var btnSav: Button

        init {
            itemImage = itemView.findViewById(R.id.image)
            itemName = itemView.findViewById(R.id.name)
            itemYear = itemView.findViewById(R.id.year)
            itemDuration = itemView.findViewById(R.id.duration)
            itemSynopsis = itemView.findViewById(R.id.synopsis)
            btnSav = itemView.findViewById(R.id.btnSave)
        }
    }
}
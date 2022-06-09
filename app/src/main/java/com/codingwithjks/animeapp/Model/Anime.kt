package com.codingwithjks.animeapp.Model

data class Anime(
    val title:String,
    val synopsis:String,
    val year:Int,
    val duration:String,
    var isSaved: Boolean = false
) {
}


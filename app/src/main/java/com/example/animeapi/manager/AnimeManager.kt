package com.example.animeapi.manager

import android.content.Context
import com.example.animeapi.model.Anime
import com.example.animeapi.model.UserAnime
import com.example.animeapi.model.db.AnimeDao
import com.example.animeapi.model.db.AppDatabase
import com.example.animeapi.model.db.UserAnimeDao
import com.example.animeapi.model.db.UserDao
import com.example.animeapi.network.ApiService

class AnimeManager(context: Context) {
    private var userAnimeDao: UserAnimeDao? = AppDatabase.createDb(context).userAnimeDao()
    private var userDao: UserDao? = AppDatabase.createDb(context).userDao()
    private var animeDao: AnimeDao? = AppDatabase.createDb(context).animeDao()

    private val watchedLabel = "watched"
    private val likedLabel = "liked"

    fun likeAnime(item: Anime) {
        markAnime(item, likedLabel)
    }

    fun watchAnime(item: Anime) {
        markAnime(item, watchedLabel)
    }

    fun getWatchedAnimes(): List<Anime> {
        val user = userDao!!.getActiveUser()
        val userAnimes = getAnimeWithLabel(user.id, watchedLabel)
        return convertUserAnimeToAnime(userAnimes)
    }

    fun getLikedAnimes(): List<Anime> {
        val user = userDao!!.getActiveUser()
        val userAnimes = getAnimeWithLabel(user.id, likedLabel)
        return convertUserAnimeToAnime(userAnimes)
    }

    suspend fun downloadPopularAnimes(): List<Anime> {
        val listResponse = ApiService.instance().getListOf20AnimeByPopularity()
        val animes = listResponse.body()?.animeList!!
        saveAnimesToDb(animes)
        return animes
    }

    suspend fun downloadAnimesByName(animeName: String): List<Anime> {
        val listResponse = ApiService.instance().getAnimeByName(animeName)
        val animes = listResponse.body()?.animeList!!
        saveAnimesToDb(animes)
        return animes
    }

    fun getPopularAnimes(): List<Anime>? {
        return animeDao?.getAnimeSortedByScore()?.take(20)
    }

    fun getAnimesByName(animeName: String): List<Anime>? {
        return animeDao?.getAnimeLikeTitle(animeName)
    }

    private fun markAnime(item: Anime, labelName: String) {
        val user = userDao?.getActiveUser()
        val anime = animeDao?.getAnimeByMalId(item.mal_id)
        if (user != null && anime != null) {
            val existingAnime =
                userAnimeDao?.getAnimesByUserAnimeAndType(user.id, anime.id, labelName)
            if (!existingAnime.isNullOrEmpty()) {
                for (exAnime in existingAnime) {
                    userAnimeDao?.deleteAnime(exAnime.id)
                }
            } else {
                userAnimeDao?.insert(UserAnime(user.id, anime.id, labelName))
            }
        }
    }

    private fun saveAnimesToDb(animes: List<Anime>) {
        for (anime in animes) {
            val animeList = animeDao?.getAnimeLikeTitle(anime.title)
            if (animeList.isNullOrEmpty()) {
                animeDao!!.insert(anime)
            }
        }
    }

    private fun getAnimeWithLabel(userId: Int, label: String): List<UserAnime>? {
        return userAnimeDao?.getAnimeWithLabelByUser(userId, label)
    }

    private fun convertUserAnimeToAnime(userAnimes: List<UserAnime>?): List<Anime> {
        val animes = mutableListOf<Anime>()
        if (userAnimes != null) {
            for (anime in userAnimes) {
                animes.add(animeDao!!.getAnimeById(anime.anime_id))
            }
        }
        return animes
    }
}
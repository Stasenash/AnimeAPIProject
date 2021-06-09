package com.example.animeapi.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapi.R
import com.example.animeapi.databinding.AnimeItemBinding
import com.example.animeapi.databinding.AnimeItemBindingImpl
import com.example.animeapi.manager.AnimeManager
import com.example.animeapi.model.Anime
import com.example.animeapi.model.UserAnime
import com.example.animeapi.model.db.AnimeDao
import com.example.animeapi.model.db.AppDatabase
import com.example.animeapi.model.db.UserAnimeDao
import com.example.animeapi.model.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AnimeAdapter : RecyclerView.Adapter<AnimeAdapter.AnimeTitleHolder>() {
    lateinit var itemBinding: AnimeItemBinding
    lateinit var animeManager : AnimeManager

    var list = listOf<Anime>()
    lateinit var mContext : Context

    fun update(list: List<Anime>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeTitleHolder {
        itemBinding = AnimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        animeManager = AnimeManager(parent.context)
        mContext = parent.context

        return AnimeTitleHolder(itemBinding)
    }

    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: AnimeTitleHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            fragmentJump(item)
        }
        val likeButton = itemBinding.heartCardButton
        likeButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    animeManager.likeAnime(item)
                }
            }
        }
        val watchedButton = itemBinding.eyeCardButton
        watchedButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    animeManager.watchAnime(item)
                }
            }
        }
    }

    private fun fragmentJump(selectedAnime: Anime) {
        val newFragment = AnimeDetailFragment()

        if (mContext is MainActivity) {
            val mainActivity = mContext as MainActivity
            mainActivity.animeSelected = selectedAnime
            mainActivity.switchContent(R.id.searchFragment, newFragment)
        }
    }

    class AnimeTitleHolder(private val itemBinding: AnimeItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Anime) {
            val image_view = itemBinding.imageView

            Glide
                .with(itemBinding.root)
                .load(item.image_url)
                .into(image_view)

            itemBinding.title.text = item.title
            itemBinding.score.text = item.score
        }

    }
}
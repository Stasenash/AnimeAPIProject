package com.example.animeapi.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapi.R
import com.example.animeapi.databinding.FragmentAnimeDetailBinding
import com.example.animeapi.databinding.RatingDialogBinding
import com.example.animeapi.model.Anime
import com.example.animeapi.model.User
import com.example.animeapi.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeDetailFragment : Fragment() {
    lateinit var anime : Anime
    private var _binding: FragmentAnimeDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _dialogBinding: RatingDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val dialogBinding get() = _dialogBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        _dialogBinding = RatingDialogBinding.inflate(inflater, container, false)

        binding.animeTitle.text = anime.title
        binding.synopsis.text = anime.synopsis
        binding.scoreField.text = anime.score
        Glide
            .with(view)
            .load(anime.image_url)
            .into(binding.animeImage)

        val dialogView = dialogBinding.root
        val builder = AlertDialog.Builder(dialogView.context)
            .setView(dialogView)
            .setTitle("Оценить аниме")
        val alertDialog = builder.create()

        binding.buttonRate.setOnClickListener {
            alertDialog.show()

            dialogBinding.buttonRateConfirm.setOnClickListener {
                val rating = dialogBinding.ratingBar.rating.times(2)
                alertDialog.dismiss()
            }

            dialogBinding.buttonClose.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _dialogBinding = null
    }
}
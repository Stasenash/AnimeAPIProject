package com.example.animeapi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeapi.R
import com.example.animeapi.databinding.FragmentPopularBinding
import com.example.animeapi.databinding.FragmentWatchedBinding
import com.example.animeapi.manager.AnimeManager
import com.example.animeapi.manager.NetworkManager
import com.example.animeapi.manager.UiHelper
import com.example.animeapi.model.Anime
import com.example.animeapi.model.db.AnimeDao
import com.example.animeapi.model.db.AppDatabase
import com.example.animeapi.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PopularFragment : Fragment() {
    private var _binding: FragmentPopularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var animeManager: AnimeManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        animeManager = AnimeManager(requireContext())

        val view = binding.root
        val nm = context?.let { NetworkManager(it) }
        val connected = nm?.isConnectedToInternet

        GlobalScope.launch(Dispatchers.IO) {
            val animes = if (connected!!) {
                animeManager.downloadPopularAnimes()
            } else {
                animeManager.getPopularAnimes()!!
            }
            withContext(Dispatchers.Main) {
                UiHelper.updateAdapter(view, binding.recView, animes)
            }
        }

        return view
    }


}
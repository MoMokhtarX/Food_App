package com.example.food_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_app.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            // In favorites screen, clicking heart removes it
            val ctx = requireContext()
            FavoritesRepository.removeFavorite(ctx, category)
            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
            loadFavorites() // Refresh list
        }
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun loadFavorites() {
        val favorites = FavoritesRepository.getFavorites(requireContext())
        categoryAdapter.updateData(favorites)

        if (favorites.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.food_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_app.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchCategories()

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            val ctx = requireContext()
            if (FavoritesRepository.isFavorite(ctx, category)) {
                FavoritesRepository.removeFavorite(ctx, category)
                Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
            } else {
                FavoritesRepository.addFavorite(ctx, category)
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvFoodItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun fetchCategories() {
        RetrofitClient.instance.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { categoryResponse ->
                        categoryAdapter.updateData(categoryResponse.categories)
                        if (categoryResponse.categories.isEmpty()) {
                            binding.emptyStateLayout.visibility = View.VISIBLE
                            binding.rvFoodItems.visibility = View.GONE
                        } else {
                            binding.emptyStateLayout.visibility = View.GONE
                            binding.rvFoodItems.visibility = View.VISIBLE
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh adapter when returning to ensure favorite states are correct if changed elsewhere
        if (::categoryAdapter.isInitialized) {
            categoryAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

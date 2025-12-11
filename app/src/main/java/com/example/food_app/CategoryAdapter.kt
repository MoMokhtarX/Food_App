package com.example.food_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoryAdapter(
    private var categories: List<Category>,
    private val onFavoriteClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoodImage: ImageView = itemView.findViewById(R.id.ivFoodImage)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        val tvFoodDescription: TextView = itemView.findViewById(R.id.tvFoodDescription)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvFoodName.text = category.strCategory
        holder.tvFoodDescription.text = category.strCategoryDescription

        // Handle Favorite Icon
        holder.ivFavorite.visibility = View.VISIBLE
        updateFavoriteIcon(holder.ivFavorite, category)

        holder.ivFavorite.setOnClickListener {
            onFavoriteClick(category)
            updateFavoriteIcon(holder.ivFavorite, category)
        }

        Glide.with(holder.itemView.context)
            .load(category.strCategoryThumb)
            .into(holder.ivFoodImage)
    }

    private fun updateFavoriteIcon(imageView: ImageView, category: Category) {
        if (FavoritesRepository.isFavorite(imageView.context, category)) {
            imageView.setImageResource(R.drawable.ic_favorite_filled) // Filled heart
            imageView.setColorFilter(imageView.context.getColor(R.color.error))
        } else {
            imageView.setImageResource(R.drawable.ic_favorite_border) // Outline heart
            imageView.setColorFilter(imageView.context.getColor(R.color.error))
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

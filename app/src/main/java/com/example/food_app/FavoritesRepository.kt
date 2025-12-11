package com.example.food_app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoritesRepository {
    private const val PREFS_NAME = "food_app_prefs"
    private const val KEY_FAVORITES = "favorites_list"
    
    private var favoriteCategories: MutableList<Category>? = null

    private fun getFavoritesList(context: Context): MutableList<Category> {
        if (favoriteCategories == null) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(KEY_FAVORITES, null)
            favoriteCategories = if (json != null) {
                val type = object : TypeToken<MutableList<Category>>() {}.type
                Gson().fromJson(json, type)
            } else {
                mutableListOf()
            }
        }
        return favoriteCategories!!
    }

    private fun saveFavorites(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favoriteCategories)
        editor.putString(KEY_FAVORITES, json)
        editor.apply()
    }

    fun addFavorite(context: Context, category: Category) {
        val list = getFavoritesList(context)
        if (list.none { it.idCategory == category.idCategory }) {
            list.add(category)
            saveFavorites(context)
        }
    }

    fun removeFavorite(context: Context, category: Category) {
        val list = getFavoritesList(context)
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().idCategory == category.idCategory) {
                iterator.remove()
                saveFavorites(context)
                break
            }
        }
    }

    fun isFavorite(context: Context, category: Category): Boolean {
        return getFavoritesList(context).any { it.idCategory == category.idCategory }
    }

    fun getFavorites(context: Context): List<Category> {
        return getFavoritesList(context).toList()
    }
}

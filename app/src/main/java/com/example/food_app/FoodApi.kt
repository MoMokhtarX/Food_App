package com.example.food_app

import retrofit2.Call
import retrofit2.http.GET

interface FoodApi {
    @GET("categories.php")
    fun getCategories(): Call<CategoryResponse>
}

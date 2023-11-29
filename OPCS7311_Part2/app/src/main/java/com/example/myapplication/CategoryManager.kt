package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Categories", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var categoryCounter = 1 // Counter for generating unique IDs

    data class CategoryName(
        val id: Int,
        val name: String,
        val imagePath: String? // Add this field to store the image path
    )

    // Function to generate a unique ID
    fun generateUniqueId(): Int {
        return categoryCounter++
    }

    fun addCategory(category: CategoryName) {
        val categories = getCategories().toMutableList()
        categories.add(category)
        saveCategories(categories)
    }

    fun removeCategory(category: CategoryName) {
        val categories = getCategories().toMutableList()
        categories.remove(category)
        saveCategories(categories)
    }

    fun getCategories(): List<CategoryName> {
        val json = sharedPreferences.getString("categories", "[]")
        return gson.fromJson(json, object : TypeToken<List<CategoryName>>() {}.type)
    }

    private fun saveCategories(categories: List<CategoryName>) {
        val json = gson.toJson(categories)
        sharedPreferences.edit().putString("categories", json).apply()
    }

    // Function to select an image for a category
    fun selectImageForCategory(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, requestCode)
    }

    // Function to handle the result of image selection and update a category
    fun handleImageSelectionResult(data: Intent?, category: CategoryName?): CategoryName? {
        if (data != null && data.data != null && category != null) {
            val selectedImageUri = data.data
            return category.copy(imagePath = selectedImageUri.toString())
        }
        return null
    }
}

class CategoryManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Categories", Context.MODE_PRIVATE)
    private val gson = Gson()



    fun addCategory(category: Category) {
        val categories = getCategories().toMutableList()
        categories.add(category)
        saveCategories(categories)

    }

    fun removeCategory(category: Category) {
        val categories = getCategories().toMutableList()
        categories.remove(category)
        saveCategories(categories)
    }



    fun getCategories(): List<Category> {
        val json = sharedPreferences.getString("categories", "[]")
        return gson.fromJson(json, object : TypeToken<List<Category>>() {}.type)
    }


    private fun saveCategories(categories: List<Category>) {
        val json = gson.toJson(categories)
        sharedPreferences.edit().putString("categories", json).apply()
    }



}
package com.driuft.random_pets_starter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var favList: MutableList<String>
    private lateinit var foodList: MutableList<String>
    private lateinit var nameList: MutableList<String>
    private lateinit var currentFood: String
    private lateinit var rvFood: RecyclerView
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        favList = getArrayListFromSharedPreferences("favoritesList")

        for (i in favList.indices) {
            Log.d("--Favorite--", favList[i].toString())
        }

        rvFood = findViewById(R.id.recyclerView)
        foodList = mutableListOf()
        nameList = mutableListOf() // Test

        getRandomFood()

        // Retrieve the saved ArrayList from SharedPreferences
        val retrievedArrayList = getArrayListFromSharedPreferences("favoritesList")

        val nextButton : Button = findViewById(R.id.nextButton)
        val prevButton : Button = findViewById(R.id.prevButton)
        val favoriteMenuButton : Button = findViewById(R.id.favorites)
        val infoText : TextView = findViewById(R.id.infoView)
        val infoScroll : ScrollView = findViewById(R.id.foodInfoView)
        infoScroll.visibility = View.INVISIBLE

        val adapter = FoodAdapter(foodList, nameList)
    }


    private fun getRandomFood() {
        // For group project
        val client = AsyncHttpClient()
        foodList.clear()
        nameList.clear()

        client["https://themealdb.com/api/json/v1/1/random.php", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Food Success", "$json")
                currentFood = json.jsonObject.getJSONArray("meals").toString()
                val randFoodArray = json.jsonObject.getJSONArray("meals")
                foodList.add(randFoodArray.getJSONObject(0).getString("strMealThumb"))
                nameList.add(randFoodArray.getJSONObject(0).getString("strMeal"))



                adapter = FoodAdapter(foodList, nameList)
                rvFood.adapter = adapter
                rvFood.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Food Error", errorResponse)
            }
        }]
    }

    private fun getFavoritesMenu() {
        foodList.clear()
        nameList.clear()

        // Update List
        favList = getArrayListFromSharedPreferences("favoritesList")

        for (i in favList) {
            foodList.add(JSONArray(i).getJSONObject(0).getString("strMealThumb"))
            nameList.add(JSONArray(i).getJSONObject(0).getString("strMeal"))
        }

        val adapter = FoodAdapter(foodList, nameList)
        rvFood.adapter = adapter
        rvFood.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    fun ignoreFood(view: View?) {
        getRandomFood()
        val icon : ImageView = findViewById(R.id.iconView)
        icon.visibility = View.VISIBLE
    }

    fun saveFood(view: View?) {
        if (currentFood.isNotEmpty()) {
            add_food_to_favorites(currentFood)
            getRandomFood()
        }
    }

    fun favoritesMenu(view: View?) {
        val icon : ImageView = findViewById(R.id.iconView)
        icon.visibility = View.INVISIBLE
        getFavoritesMenu()

    }

    fun exitInfo(view: View?) {
        var scrollView : ScrollView = findViewById(R.id.foodInfoView)
        scrollView.visibility = View.INVISIBLE
    }

    fun showInfo(view: View?) {
        if (foodList.size == favList.size) {
            if (view != null) {
                var position : View? = rvFood.layoutManager?.findContainingItemView(view)
                if (position != null) {
                    Log.d("Please work", rvFood.layoutManager?.getPosition(position).toString())
                    currentFood = favList[rvFood.layoutManager?.getPosition(position)!!]
                }

            }

            Log.d("Test", foodList.toString())
            Log.d("TEst2", favList.toString())

        }
        getFoodInfo(JSONArray(currentFood), findViewById(R.id.infoView), findViewById(R.id.foodInfoView))
    }

    fun add_food_to_favorites(food : String) {
        favList.add(food)
        saveArrayListToSharedPreferences("favoritesList", favList)
    }

    private fun saveArrayListToSharedPreferences(key: String, arrayList: MutableList<String>) {
        // Convert the ArrayList to a Set of strings
        val mySet = arrayList.toSet()

        // Get an instance of the SharedPreferences
        val sharedPreferences = getSharedPreferences("favoritesList", Context.MODE_PRIVATE)

        // Get an instance of the SharedPreferences.Editor
        val editor = sharedPreferences.edit()

        // Put the Set of strings into the SharedPreferences.Editor
        editor.putStringSet(key, mySet)

        // Apply the changes to the SharedPreferences
        editor.apply()
    }

    private fun getArrayListFromSharedPreferences(key: String): ArrayList<String> {
        // Get an instance of the SharedPreferences
        val sharedPreferences = getSharedPreferences("favoritesList", Context.MODE_PRIVATE)

        // Retrieve the Set of strings from SharedPreferences
        val mySet = sharedPreferences.getStringSet(key, emptySet())

        // Convert the Set of strings back to an ArrayList
        val myArrayList = arrayListOf<String>()
        myArrayList.addAll(mySet!!)

        return myArrayList
    }

    private fun getFoodInfo(food: JSONArray, infoText: TextView, scrollView: ScrollView) {
        scrollView.visibility = View.VISIBLE
        var ingredient = 1
        var info : String = food.getJSONObject(0).getString("strMeal") + "\n"
        info += "Category: " + food.getJSONObject(0).getString("strCategory") + "\n"
        info += "Origin: " + food.getJSONObject(0).getString("strArea") + "\n"
        info += "\nIngredients\n"
        while (ingredient <= 20) {
            if (food.getJSONObject(0).getString("strIngredient$ingredient") != "null")
                if (food.getJSONObject(0).getString("strIngredient$ingredient") != "") {
                    info += food.getJSONObject(0).getString("strIngredient$ingredient") + " : "
                    info += food.getJSONObject(0).getString("strMeasure$ingredient") + "\n"
                }
            ingredient += 1
        }
        info += "\nInstructions\n" + food.getJSONObject(0).getString("strInstructions") + "\n"
        info += "\nYouTube Video: " + food.getJSONObject(0).getString("strYoutube") + "\n"
        info += "\nSource: " + food.getJSONObject(0).getString("strSource") + "\n"

        infoText.text = info

    }
}
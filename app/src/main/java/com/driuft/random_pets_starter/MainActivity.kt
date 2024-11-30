package com.driuft.random_pets_starter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var petList: MutableList<String>
    private lateinit var nameList: MutableList<String> // Test
    private lateinit var rvPets: RecyclerView
    private var startNumber : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPets = findViewById(R.id.recyclerView)
        petList = mutableListOf()
        nameList = mutableListOf() // Test

        //getDogImageURL()
        getPokemonImageURL(startNumber)

        val nextButton : Button = findViewById(R.id.nextButton)
        val prevButton : Button = findViewById(R.id.prevButton)
    }

    /*
    private fun getDogImageURL() {
        val client = AsyncHttpClient()

        client["https://dog.ceo/api/breeds/image/random/20", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog Success", "$json")
                val petImageArray = json.jsonObject.getJSONArray("message")

                for (i in 0 until petImageArray.length()) {
                    petList.add(petImageArray.getString(i))
                }

                val adapter = PetAdapter(petList)
                rvPets.adapter = adapter
                rvPets.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }
    */


    private fun getPokemonImageURL(startNum: Int) {
        petList.clear()
        nameList.clear() // Test
        val client = AsyncHttpClient()
        val endNum : Int = startNum + 20
        for (i in startNum until endNum) {
            client["https://pokeapi.co/api/v2/pokemon/$i/", object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                    Log.d("Pokemon Success", "$json")
                    Log.d("Pokemon Name", json.jsonObject.getString("name"))
                    petList.add(json.jsonObject.getJSONObject("sprites").getString("front_default"))
                    nameList.add(" ID: $i,  " + "name: "  + json.jsonObject.getString("name"))

                    if (i == endNum - 1) {
                        val adapter = PetAdapter(petList, nameList)
                        rvPets.adapter = adapter
                        rvPets.layoutManager = LinearLayoutManager(this@MainActivity)
                        Log.d("Pokemon List", petList.toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    throwable: Throwable?
                ) {
                    Log.d("Pokemon Error", errorResponse)
                }
            }]
        }
    }

    fun nextTwenty(view: View?) {
        if (startNumber != 1001) {
            startNumber += 20
            getPokemonImageURL(startNumber)
        }
    }

    fun prevTwenty(view: View?) {
        if (startNumber != 1) {
            startNumber -= 20
            getPokemonImageURL(startNumber)
        }
    }
}
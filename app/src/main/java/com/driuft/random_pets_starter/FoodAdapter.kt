package com.driuft.random_pets_starter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import org.json.JSONArray

class FoodAdapter (private val foodList: List<String>, private val nameList: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    lateinit var nameButton: Button
    private var onClickListener: OnClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var foodImage: ImageView

        // var nameView: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            foodImage = view.findViewById(R.id.pet_image)
            // nameView = view.findViewById(R.id.nameView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // val item = foodList[position]
        nameButton = holder.itemView.findViewById(R.id.nameButton)


        Glide.with(holder.itemView)
            .load(foodList[position])
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.pet_image))
        nameButton.text = nameList[position]
    }
}
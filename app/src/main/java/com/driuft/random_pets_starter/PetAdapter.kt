package com.driuft.random_pets_starter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class PetAdapter (private val petList: List<String>, private val nameList: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    lateinit var nameView: TextView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var petImage: ImageView
        // var nameView: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            petImage = view.findViewById(R.id.pet_image)
            // nameView = view.findViewById(R.id.nameView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pet_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = petList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        nameView = holder.itemView.findViewById(R.id.nameView)
        Glide.with(holder.itemView)
            .load(petList[position])
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.pet_image))
        nameView.text = nameList[position]
    }

}
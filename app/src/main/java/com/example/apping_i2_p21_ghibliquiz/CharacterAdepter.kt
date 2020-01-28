package com.example.apping_i2_p21_ghibliquiz

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdepter (val data: List<CharacterItem>,
                        val context: Activity,
                        val onItemClickListener: View.OnClickListener):RecyclerView.Adapter<CharacterAdepter.ViewHolder>() {

    class ViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val age: TextView    = rowView.findViewById(R.id.textViewAge)
        val image: ImageView = rowView.findViewById(R.id.imageViewGender)
        val name : TextView  = rowView.findViewById(R.id.textViewName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.character_list_item,
            parent,
            false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character_info: CharacterItem = data[position]
        holder.age.text  = character_info.age
        holder.name.text = character_info.name

        holder.itemView.tag = position
        if (character_info.gender == "Female") {
            holder.image.setImageResource(R.drawable.female)
            return
        }

        holder.image.setImageResource(R.drawable.male)
    }
}
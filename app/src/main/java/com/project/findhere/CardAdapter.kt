package com.project.findhere

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide

class CardAdapter (val context: Context,val CardList: List<ProfileCard>)
    : Adapter<com.project.findhere.CardAdapter.ViewHolder>(){
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val cardImage : ImageView = view.findViewById(R.id.profileImage)
            val cardContent : TextView = view.findViewById(R.id.profileContent)
            val cardSelection : TextView = view.findViewById(R.id.profileSelection)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = CardList[position]
        holder.cardContent.text = card.content
        holder.cardSelection.text = card.selection
        Glide.with(context).load(card.imageId).into(holder.cardImage)
    }

    override fun getItemCount() = CardList.size

}
package com.project.findhere

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

class ProfileCardAdapter (val context: Context, val CardList: List<ProfileCard>,
                          private val listener : OnCardClick)
    : Adapter<com.project.findhere.ProfileCardAdapter.ViewHolder>(){
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val cardImage : ImageView = view.findViewById(R.id.profileImage)
            val cardContent : TextView = view.findViewById(R.id.profileContent)
            val cardSelection : TextView = view.findViewById(R.id.profileSelection)
            val cardView : MaterialCardView = view.findViewById(R.id.xmlprofilecard)
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
        holder.cardView.setOnLongClickListener {
            if(listener.editable){ listener.clickAction(position) }
            true
        }
    }

    override fun getItemCount() = CardList.size

    interface OnCardClick{
        fun clickAction(position: Int)
        var editable : Boolean
    }
}

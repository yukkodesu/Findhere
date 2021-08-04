package com.project.findhere

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.findhere.models.Post

class FoundPostAdapter (val context : Context , val posts : List<Post>) : RecyclerView.Adapter<FoundPostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(post : Post){
            val cardusername : AppCompatTextView = itemView.findViewById(R.id.cardusername)
            cardusername.text = post.user?.username
            val carddescription : AppCompatTextView = itemView.findViewById(R.id.carddescription)
            carddescription.text = post.description
            val cardimage : ImageView = itemView.findViewById(R.id.cardimage)
            Glide.with(context).load(post.imgurl).into(cardimage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_posts,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
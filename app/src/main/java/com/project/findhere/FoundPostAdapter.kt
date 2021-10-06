package com.project.findhere

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.project.findhere.models.FoundPost
import de.hdodenhof.circleimageview.CircleImageView

class FoundPostAdapter (val context : Context, private val foundPosts : List<FoundPost>) : RecyclerView.Adapter<FoundPostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(foundPost : FoundPost){
            val cardusername : AppCompatTextView = itemView.findViewById(R.id.cardusername)
            cardusername.text = foundPost.user?.username
            val carddescription : AppCompatTextView = itemView.findViewById(R.id.carddescription)
            carddescription.text = foundPost.description
            val cardimage : ImageView = itemView.findViewById(R.id.cardimage)
            val cardavatar : CircleImageView = itemView.findViewById(R.id.cardavatar)
            Glide.with(context).load(foundPost.imgurl).into(cardimage)
            Glide.with(context).load(foundPost.user?.avatarurl).into(cardavatar)
            val expandview : ConstraintLayout = itemView.findViewById(R.id.expandableLayout)
            val cardview : CardView = itemView.findViewById(R.id.foundcard)
            val cardarrowBtn : Button = itemView.findViewById(R.id.cardarrowBtn)
            cardarrowBtn.setOnClickListener{
                if(expandview.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(cardview, AutoTransition())
                    expandview.visibility = View.VISIBLE
                    cardarrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                }
                else {
                    expandview.visibility = View.GONE
                    cardarrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foundposts,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(foundPosts[position])

    }

    override fun getItemCount(): Int {
        return foundPosts.size
    }
}
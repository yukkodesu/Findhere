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
import com.project.findhere.models.FindPost
import de.hdodenhof.circleimageview.CircleImageView

class FindPostAdapter (val context : Context, private val findPosts : List<FindPost>) : RecyclerView.Adapter<FindPostAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(findPost : FindPost){
            val cardusername : AppCompatTextView = itemView.findViewById(R.id.findcardusername)
            cardusername.text = findPost.user?.username
            val carddescription : AppCompatTextView = itemView.findViewById(R.id.findcarddescription)
            carddescription.text = findPost.description
            val cardavatar : CircleImageView = itemView.findViewById(R.id.findcardavatar)
            Glide.with(context).load(findPost.user?.avatarurl).into(cardavatar)
            val expandview : ConstraintLayout = itemView.findViewById(R.id.findexpandableLayout)
            val cardview : CardView = itemView.findViewById(R.id.findcard)
            val cardarrowBtn : Button = itemView.findViewById(R.id.findcardarrowBtn)
            val cardimage : ImageView = itemView.findViewById(R.id.findcardimage)
            if(findPost.imgurl != ""){
                Glide.with(context).load(findPost.imgurl).into(cardimage)
            }
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
            val cardfinddate : AppCompatTextView = itemView.findViewById(R.id.findtime)
            cardfinddate.text = findPost.lostdate
            val cardplace : AppCompatTextView = itemView.findViewById(R.id.findcardloc)
            cardplace.text = findPost.place
            val cardname : AppCompatTextView = itemView.findViewById(R.id.findcardtitle)
            cardname.text = findPost.name
            cardname.isSelected = true
            val cardemail : AppCompatTextView = itemView.findViewById(R.id.findmailNumber)
            cardemail.text = findPost.user?.display_email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_findposts,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(findPosts[position])

    }

    override fun getItemCount(): Int {
        return findPosts.size
    }
}
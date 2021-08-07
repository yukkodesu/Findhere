package com.project.findhere

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.project.findhere.models.User
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {


    val cardList = ArrayList<ProfileCard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val firebaseDb = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val documentRef = firebaseDb.collection("users").document("${auth.currentUser?.uid}")
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val thisuser = document.toObject<User>()!!
                    initCards(thisuser,auth)
                    initRv()
                    val profile_avatar : CircleImageView = findViewById(R.id.profileavatar)
                    Glide.with(this).load(thisuser.avatarurl).into(profile_avatar)
                } else {
                    Log.d("ProfileActivity", "get userinfo failed with no userid exist")
                }
            }

        val button : MaterialButton = findViewById(R.id.profile_backButton)
        button.setOnClickListener(){
            onBackPressed()
        }

    }

    private fun initRv(){
        val layoutManager = GridLayoutManager(this, 2)
        val recyclerView: RecyclerView = findViewById(R.id.profile_recyclerview)
        recyclerView.layoutManager = layoutManager
        val adapter = CardAdapter(this, cardList)
        recyclerView.adapter = adapter
    }

    private fun initCards(user : User,auth : FirebaseAuth) {
        cardList.clear()
        cardList.add(ProfileCard("账号", "${auth.currentUser?.email}", R.drawable.ic_baseline_person1_24))
        cardList.add(ProfileCard("用户名", "${user.username}", R.drawable.ic_baseline_notes_24))
        cardList.add(ProfileCard("QQ", "${user.qq}", R.drawable.ic_baseline_connect_without_contact_24))
        cardList.add(ProfileCard("邮箱", "${auth.currentUser?.email}", R.drawable.ic_baseline_local_post_office_24))
        cardList.add(ProfileCard("年级", "${user.grade}", R.drawable.ic_baseline_create_24))
    }
}
package com.project.findhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    val cards = mutableListOf(
        ProfileCard("账号", "lixingzuo@test.com", R.drawable.ic_baseline_person1_24),
        ProfileCard("用户名", "lxz", R.drawable.ic_baseline_notes_24),
        ProfileCard("电话号码", "123", R.drawable.ic_baseline_local_phone_24),
        ProfileCard("QQ", "2738280858", R.drawable.ic_baseline_connect_without_contact_24),
        ProfileCard("邮箱", "lixingzuo@test.com", R.drawable.ic_baseline_local_post_office_24),
        ProfileCard("个人描述", "...", R.drawable.ic_baseline_create_24)
    )
    val cardList = ArrayList<ProfileCard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initCards()
        val layoutManager = GridLayoutManager(this, 2)
        val recyclerView: RecyclerView = findViewById(R.id.profile_recyclerview)
        recyclerView.layoutManager = layoutManager
        val adapter = CardAdapter(this, cardList)
        recyclerView.adapter = adapter

        val button : MaterialButton = findViewById(R.id.profile_backButton)
        button.setOnClickListener(){
            onBackPressed()
        }

    }

    private fun initCards() {
        cardList.clear()
        for(items in cards){
            cardList.add(items)
        }
    }
}
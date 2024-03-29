package com.project.findhere

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.project.findhere.models.User
import de.hdodenhof.circleimageview.CircleImageView
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater

import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginLeft
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.SetOptions


class ProfileActivity : AppCompatActivity() {

    private var isEditable = false
    private val firebaseDb = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val documentRef = firebaseDb.collection("users").document("${auth.currentUser?.uid}")
    private val cardList = ArrayList<ProfileCard>()
    private var AlertDialogInput = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val thisuser = document.toObject<User>()!!
                    initCards(thisuser, auth)
                    initRv()
                    val profile_avatar: CircleImageView = findViewById(R.id.profileavatar)
                    if (thisuser.avatarurl != "")
                        Glide.with(this).load(thisuser.avatarurl).into(profile_avatar)
                } else {
                    Log.d("ProfileActivity", "get userinfo failed with no userid exist")
                }
            }
        val button1: MaterialButton = findViewById(R.id.profile_backButton)
        button1.setOnClickListener() {
            onBackPressed()
        }
    }

    private fun initRv() {
        val layoutManager = GridLayoutManager(this, 2)
        val recyclerView: RecyclerView = findViewById(R.id.profile_recyclerview)
        recyclerView.layoutManager = layoutManager
        val listener = object : ProfileCardAdapter.OnCardClick {
            override fun clickAction(position: Int) {
                Log.d("ProfileActivity", "OKFine")
                handleProfileCardClick(position)
            }

            override var editable = isEditable
        }

        val button2: FloatingActionButton = findViewById(R.id.profile_fab)
        val titletext: TextView = findViewById(R.id.profiletitletextview)
        button2.setOnClickListener {
            if (!isEditable) {
                button2.setImageResource(R.drawable.ic_baseline_check_24)
                titletext.setText(R.string.pressCardtoEdit)
                listener.editable = true
                isEditable = true
            } else {
                button2.setImageResource(R.drawable.ic_baseline_create_24)
                titletext.setText(R.string.profile_center)
                listener.editable = false
                isEditable = false
            }
        }
        val adapter = ProfileCardAdapter(this, cardList, listener)
        recyclerView.adapter = adapter
    }

    private fun initCards(user: User, auth: FirebaseAuth) {
        cardList.clear()
        cardList.add(
            ProfileCard(
                "账号",
                "${auth.currentUser?.email}",
                R.drawable.ic_baseline_person1_24
            )
        )
        cardList.add(ProfileCard("用户名", "${user.username}", R.drawable.ic_baseline_notes_24))
        cardList.add(
            ProfileCard(
                "QQ",
                "${user.qq}",
                R.drawable.ic_baseline_connect_without_contact_24
            )
        )
        cardList.add(
            ProfileCard(
                "邮箱",
                "${auth.currentUser?.email}",
                R.drawable.ic_baseline_local_post_office_24
            )
        )
        cardList.add(ProfileCard("年级", "${user.grade}", R.drawable.ic_baseline_calendar_today_24))
        cardList.add(ProfileCard("手机号","${user.phone}",R.drawable.ic_baseline_phone_24))
    }

    private fun handleProfileCardClick(position : Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val v = LayoutInflater.from(this).inflate(R.layout.profile_alertdialog,null)
        builder.setView(v)
        val edittext : EditText = v.findViewById(R.id.alertdialogtext)
        val title : TextView = v.findViewById(R.id.alertdialogTitle)
        val titletext = when(position){
            1 -> "用户名"
            2 -> "QQ"
            3 -> "联系邮箱"
            5 -> "电话"
            else -> ""
        }
        title.text = "修改${titletext}"
        builder.setPositiveButton("确认",
            DialogInterface.OnClickListener { dialog, which -> AlertDialogInput = edittext.text.toString()
                documentRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null){
                            val thisuser = document.toObject<User>()!!
                            if (position == 1){
                                thisuser.username = AlertDialogInput
                            }
                            else if (position == 2){
                                thisuser.qq = AlertDialogInput
                            }
                            else if (position == 3){
                                thisuser.display_email = AlertDialogInput
                            }
                            else if (position == 5){
                                thisuser.phone = AlertDialogInput
                            }
                            documentRef.set(thisuser,
                                SetOptions.merge())
                                .addOnSuccessListener {
                                    Log.d("ProfileInfoChangeAlert",AlertDialogInput+"${position}")
                                }
                        }else {
                            Log.d("ProfileActivity", "get userinfo failed with no userid exist")
                        }
                    }
            })
        builder.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}
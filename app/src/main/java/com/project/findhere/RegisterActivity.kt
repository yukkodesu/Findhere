package com.project.findhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth

        val etEmail = findViewById<EditText>(R.id.register_EmailAddress)
        val etPassword = findViewById<EditText>(R.id.register_Password)
        val etConfirm = findViewById<EditText>(R.id.register_ConfirmPassword)

        val regbtn = findViewById<Button>(R.id.regActivitybutton)
        regbtn.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(password == etConfirm.text.toString()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(this,"注册成功,快去登录吧",Toast.LENGTH_SHORT)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "注册失败",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this,"已有用户登入，请注销后再试",Toast.LENGTH_SHORT)
            finish()
        }
    }
}
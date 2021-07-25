package com.project.findhere

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login_toolbar : Toolbar = findViewById(R.id.login_toolbar)
        login_toolbar.title = getString(R.string.login_title)
        setSupportActionBar(login_toolbar)
        val loginbtn : Button = findViewById(R.id.login_button)
        val etEmail : EditText = findViewById(R.id.login_EmailAddress)
        val etPassword : EditText = findViewById(R.id.login_Password)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            goMainActivity()
        }

        loginbtn.setOnClickListener {

            loginbtn.isEnabled = false
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(email.isBlank()||password.isBlank()) {
                Toast.makeText(this,"邮箱/密码不能为空",Toast.LENGTH_SHORT).show()
                loginbtn.isEnabled = true
                return@setOnClickListener
            }
            // Firebase authentication check

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                Log.d(TAG,"Now logging")
                loginbtn.isEnabled = true
                if (task.isSuccessful){
                    Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show()
                    goMainActivity()
                }else{
                    Log.i(TAG,"登陆失败",task.exception)
                    Toast.makeText(this,"密码/邮箱错误",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goMainActivity(){
        Log.i(TAG,"goMainActivity")
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
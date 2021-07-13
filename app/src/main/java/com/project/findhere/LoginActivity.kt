package com.project.findhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login_toolbar : Toolbar = findViewById(R.id.login_toolbar)
        login_toolbar.title = getString(R.string.login_title)
        setSupportActionBar(login_toolbar)
        val loginbtn : Button = findViewById(R.id.login_button)
        loginbtn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
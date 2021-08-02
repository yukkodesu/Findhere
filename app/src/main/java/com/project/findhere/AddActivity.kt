package com.project.findhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val button : MaterialButton = findViewById(R.id.add_backButton)
        button.setOnClickListener(){
            onBackPressed()
        }
    }
}
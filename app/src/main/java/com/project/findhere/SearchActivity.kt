package com.project.findhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val button :MaterialButton = findViewById(R.id.search_backButton)
        button.setOnClickListener {
            onBackPressed()
        }
    }
}
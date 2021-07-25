package com.project.findhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.project.findhere.R.id.mainSpace as mainSpace1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainToolbar : Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }
        val navView : NavigationView = findViewById(R.id.main_navView)
        navView.setCheckedItem(mainSpace1)
        navView.setNavigationItemSelectedListener {

            if (it.itemId == R.id.mainSpace){
                val intent = Intent(this,ProfileActivity::class.java)
                startActivity(intent)
            }

            val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onBackPressed() {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

}

package com.project.findhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.TableLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
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
           when(it.itemId){
               R.id.mainProfile ->{
                   val intent = Intent(this,ProfileActivity::class.java)
                   startActivity(intent)
               }
               R.id.mainQuit -> {
                   FirebaseAuth.getInstance().signOut()
                   val intent = Intent(this,LoginActivity::class.java)
                   startActivity(intent)
                   finish()
               }
           }
            val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
            drawerLayout.closeDrawers()
            true
        }

        //setup Viewpager
        val viewpager : ViewPager2 = findViewById(R.id.viewpager)
        val tabs : TabLayout = findViewById(R.id.tab)
        val adapter = FragmentAdapter(supportFragmentManager,lifecycle)
        viewpager.adapter = adapter
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager.setCurrentItem(tab.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabs.selectTab(tabs.getTabAt(position))
            }
        })

        // end setup viewpager

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

    override fun onResume() {
        super.onResume()
        val navView : NavigationView = findViewById(R.id.main_navView)
        navView.setCheckedItem(mainSpace1)
    }
}

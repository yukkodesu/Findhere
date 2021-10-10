package com.project.findhere

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.project.findhere.models.User
import de.hdodenhof.circleimageview.CircleImageView
import com.project.findhere.R.id.mainSpace as mainSpace1

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseDb: FirebaseFirestore

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
               R.id.mainSearch ->{
                   val intent = Intent(this,SearchActivity::class.java)
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

        //add floatButton
        tabs.selectedTabPosition
        val fab : FloatingActionButton = findViewById(R.id.main_fab)
        fab.setOnClickListener{
            val intent = Intent(this,AddActivity::class.java).apply {
                putExtra("TabSelected",tabs.selectedTabPosition)
            }
            startActivity(intent)
        }
        //floatButton end

        //setting up userinfo
        val auth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
        val documentRef = firebaseDb.collection("users").document("${auth.currentUser?.uid}")
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val thisuser = document.toObject<User>()
                    if(thisuser?.avatarurl != ""){
                        val mainavatar : CircleImageView = findViewById(R.id.main_avatar)
                        Glide.with(this).load(thisuser?.avatarurl).into(mainavatar)
                    }
                    val mainusername : TextView = findViewById(R.id.main_Username)
                    mainusername.text = thisuser?.username
                } else {
                    Log.d("MainActivity", "get userinfo failed with no userid exist")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "get userinfo with ", exception)
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

    override fun onResume() {
        super.onResume()
        val navView : NavigationView = findViewById(R.id.main_navView)
        navView.setCheckedItem(mainSpace1)
    }
}

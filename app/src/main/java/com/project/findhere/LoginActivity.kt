package com.project.findhere

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login_toolbar: Toolbar = findViewById(R.id.login_toolbar)
        login_toolbar.title = getString(R.string.login_title)
        setSupportActionBar(login_toolbar)
        val loginbtn: Button = findViewById(R.id.login_button)
        val etEmail: EditText = findViewById(R.id.login_EmailAddress)
        val etPassword: EditText = findViewById(R.id.login_Password)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goMainActivity()
        }
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        var isSave = false
        val rememberCheck: CheckBox = findViewById(R.id.RemembercheckBox)
        val defvalemail = resources.getString(R.string.login_emailAddress)
        val defvalpassword = resources.getString(R.string.login_password)
        val saveEmail = sharedPref.getString("email",defvalemail)
        val savePassword = sharedPref.getString("password",defvalpassword)
        if(saveEmail != defvalemail){
            etEmail.setText(saveEmail)
            etPassword.setText(savePassword)
            rememberCheck.isChecked = true
            isSave = true
        }
        rememberCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            isSave = if (isChecked) {
                true
            }else{
                with(sharedPref.edit()){
                    putString("email",defvalemail)
                    putString("password",defvalpassword)
                    commit()
                }
                false
            }
        })

        loginbtn.setOnClickListener {

            loginbtn.isEnabled = false
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(isSave){
                with(sharedPref.edit()){
                    putString("email",email)
                    putString("password",password)
                    commit()
                }
            }
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "邮箱/密码不能为空", Toast.LENGTH_SHORT).show()
                loginbtn.isEnabled = true
                return@setOnClickListener
            }


            // Firebase authentication check

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                Log.d(TAG, "Now logging")
                loginbtn.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show()
                    goMainActivity()
                } else {
                    Log.i(TAG, "登陆失败", task.exception)
                    Toast.makeText(this, "密码/邮箱错误", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //setup registerButton
        val registerBtn : Button = findViewById(R.id.reg_button)
        registerBtn.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun goMainActivity() {
        Log.i(TAG, "goMainActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
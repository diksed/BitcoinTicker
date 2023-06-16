package com.sedatkavak.bitcointicker.ui.account.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.sedatkavak.bitcointicker.R
import com.sedatkavak.bitcointicker.ui.MainActivity
import com.sedatkavak.bitcointicker.ui.account.register.RegisterFragment

class LoginActivity : AppCompatActivity(), LoginFragment.OnCreateAccountButtonClickListener {

    private var isRegisterFragmentVisible = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (savedInstanceState == null) {
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        } else {
            isRegisterFragmentVisible = savedInstanceState.getBoolean("IS_REGISTER_FRAGMENT_VISIBLE", false)
            if (isRegisterFragmentVisible) {
                supportFragmentManager.popBackStackImmediate()
            }
        }

    }
    private fun isLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("IS_REGISTER_FRAGMENT_VISIBLE", isRegisterFragmentVisible)
    }
    override fun onCreateAccountButtonClicked() {
        val registerFragment = RegisterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, registerFragment)
            .addToBackStack(null)
            .commit()
        isRegisterFragmentVisible = true
    }
}

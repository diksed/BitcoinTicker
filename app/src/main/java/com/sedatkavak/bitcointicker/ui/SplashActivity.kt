package com.sedatkavak.bitcointicker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sedatkavak.bitcointicker.R

import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.sedatkavak.bitcointicker.ui.account.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashImageView: ImageView = findViewById(R.id.splashImageView)
        val splashGif: DrawableImageViewTarget =
            DrawableImageViewTarget(splashImageView)
        Glide.with(this)
            .load(R.drawable.splash)
            .into(splashGif)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, SPLASH_DELAY)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_DELAY = 3000L // 3 saniye
    }
}


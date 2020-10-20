package com.example.githubuserappv03

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import gr.net.maroulis.library.EasySplashScreen

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        val config = EasySplashScreen(this@SplashScreenActivity)
            .withFullScreen()
            .withTargetActivity(MainActivity::class.java)
            .withSplashTimeOut(2500)
            .withBackgroundColor(Color.parseColor("#000000"))
            .withFooterText("By mrputraa")
            .withLogo(R.mipmap.github_round_logo)

        config.footerTextView.setTextColor(Color.WHITE)

        val easySplashScreen: View = config.create()
        setContentView(easySplashScreen)

    }
}

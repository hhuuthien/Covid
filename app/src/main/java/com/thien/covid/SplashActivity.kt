package com.thien.covid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LoadActivity::class.java))
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
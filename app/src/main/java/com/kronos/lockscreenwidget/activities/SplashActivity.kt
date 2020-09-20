package com.kronos.lockscreenwidget.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kronos.lockscreenwidget.R
import gr.net.maroulis.library.EasySplashScreen

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var easySplash = EasySplashScreen(this)
            .withFullScreen()
            .withTargetActivity(PermissionActivity::class.java)
            .withSplashTimeOut(1500)
            .withFullScreen()
        easySplash.create()
    }
}

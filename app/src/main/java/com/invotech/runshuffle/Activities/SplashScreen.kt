package com.invotech.runshuffle.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.invotech.runshuffle.R
import java.lang.Exception

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val background = object : Thread()
        {
            override fun run() {
                try {
                    Thread.sleep(1000)
                    startActivity(Intent(this@SplashScreen,LoginActivity::class.java))

                } catch (e: Exception)
                {
                    e.printStackTrace()
                }

            }
        }
        background.start()
    }
}
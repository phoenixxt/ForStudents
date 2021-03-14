package com.githubuiviewer.newpackage.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.githubuiviewer.R
import com.githubuiviewer.newpackage.app.Navigator

class MainActivity : AppCompatActivity() {
    private val startApplicationController = MainActivityComponent.createStartApplicationController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigator.provideActivity(this)

        if (savedInstanceState == null) {
            startApplicationController.onStart(intent)
        }
    }
}

package com.githubuiviewer.newpackage.app.activity

import com.githubuiviewer.newpackage.UserDataStorage
import com.githubuiviewer.newpackage.app.AppComponent
import com.githubuiviewer.newpackage.app.Navigator
import com.githubuiviewer.newpackage.start.StartApplicationController

object MainActivityComponent {
    fun createStartApplicationController(): StartApplicationController {
        return StartApplicationController(
            UserDataStorage(AppComponent.sharedPreferences),
            Navigator
        )
    }
}

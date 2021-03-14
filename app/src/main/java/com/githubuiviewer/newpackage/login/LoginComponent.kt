package com.githubuiviewer.newpackage.login

import com.githubuiviewer.newpackage.UserDataStorage
import com.githubuiviewer.newpackage.app.AppComponent
import com.githubuiviewer.newpackage.login.data.LoginRepository

object LoginComponent {

    fun createLoginRepository(): LoginRepository {
        return LoginRepository(
            gitHubService = AppComponent.gitHubService,
            userDataStorage = UserDataStorage(AppComponent.sharedPreferences)
        )
    }
}

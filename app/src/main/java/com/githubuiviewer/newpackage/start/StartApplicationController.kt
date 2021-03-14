package com.githubuiviewer.newpackage.start

import android.content.Intent
import android.net.Uri
import com.githubuiviewer.newpackage.UserDataStorage
import com.githubuiviewer.newpackage.app.Navigator
import com.githubuiviewer.newpackage.redirectUrl

class StartApplicationController(
    private val userDataStorage: UserDataStorage,
    private val navigator: Navigator
) {

    fun onStart(intent: Intent) {
        val code = getCodeFromUri(intent.data)

        when {
            code != null -> navigator.openUpdateTokenFragment(code)
            userDataStorage.token.isEmpty() -> navigator.openLogin()
            else -> navigator.openCurrentUserScreen()
        }
    }

    private fun getCodeFromUri(uri: Uri?): String? {
        uri ?: return null
        if (!uri.toString().startsWith(redirectUrl)) {
            return null
        }
        return uri.getQueryParameter("code")
    }
}

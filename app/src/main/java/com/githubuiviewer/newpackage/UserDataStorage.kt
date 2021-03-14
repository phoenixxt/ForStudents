package com.githubuiviewer.newpackage

import android.content.SharedPreferences
import com.githubuiviewer.old.tools.sharedPrefsTools.SharedPrefDelegate

class UserDataStorage(sharedPreferences: SharedPreferences) {

    private companion object {
        const val KEY_TOKEN = "KEY_TOKEN"
    }

    var token: String by SharedPrefDelegate(sharedPreferences, KEY_TOKEN, "")
}
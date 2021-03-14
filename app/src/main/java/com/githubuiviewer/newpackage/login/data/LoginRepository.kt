package com.githubuiviewer.newpackage.login.data

import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.newpackage.UserDataStorage
import com.githubuiviewer.old.tools.clientId
import com.githubuiviewer.old.tools.clientSecret

class LoginRepository(
    private val gitHubService: GitHubService,
    private val userDataStorage: UserDataStorage,
) {
    suspend fun updateToken(code: String) {
        val token = gitHubService.getAccessToken(clientId, clientSecret, code)
        userDataStorage.token = token.accessToken
    }
}
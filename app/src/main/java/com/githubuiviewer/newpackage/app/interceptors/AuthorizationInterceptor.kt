package com.githubuiviewer.newpackage.app.interceptors

import com.githubuiviewer.newpackage.UserDataStorage
import com.githubuiviewer.newpackage.app.Navigator
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val navigator: Navigator,
    private val userDataStorage: UserDataStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", userDataStorage.token)
            .build()

        val result = chain.proceed(request)

        if (result.code == 401) {
            navigator.openLogin()
        }

        return result
    }
}

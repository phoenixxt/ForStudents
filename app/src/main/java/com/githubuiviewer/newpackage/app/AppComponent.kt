package com.githubuiviewer.newpackage.app

import android.content.Context
import android.content.SharedPreferences
import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.newpackage.app.interceptors.AuthorizationInterceptor
import com.githubuiviewer.old.tools.host
import com.githubuiviewer.old.tools.schema
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppComponent {

    private lateinit var context: Context

    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            com.githubuiviewer.old.tools.PRIVATE_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().client(
            OkHttpClient().newBuilder()
                .addInterceptor(AuthorizationInterceptor(Navigator))
                .build()
        ).baseUrl(HttpUrl.Builder().scheme(schema).host(host).build())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
    }

    val gitHubService: GitHubService by lazy {
        retrofit.create(GitHubService::class.java)
    }

    fun init(context: Context) {
        this.context = context
    }

}

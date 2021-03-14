package com.githubuiviewer.old.di

import android.content.Context
import com.githubuiviewer.old.data.repository.GitHubRepository
import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.newpackage.app.Navigator
import com.githubuiviewer.old.datasource.api.ErrorInterceptor
import com.githubuiviewer.old.datasource.api.HeaderInterceptor
import com.githubuiviewer.old.tools.host
import com.githubuiviewer.old.tools.schema
import com.githubuiviewer.old.tools.sharedPrefsTools.SharedPref
import com.githubuiviewer.old.ui.BaseViewModel
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun context(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideGitHubReposApi(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(gitHubService: GitHubService, sharedPref: SharedPref): GitHubRepository {
        return GitHubRepository(gitHubService, sharedPref)
    }

    @Provides
    @Singleton
    fun provideSharedPref(context: Context): SharedPref {
        return SharedPref(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, sharedPref: SharedPref, context: Context): Retrofit {
        return Retrofit.Builder().client(
            OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(HeaderInterceptor(sharedPref))
                .addInterceptor(ErrorInterceptor(context))
                .build()
        ).baseUrl(HttpUrl.Builder().scheme(schema).host(host).build())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        )
    }

    @Provides
    @Singleton
    fun provideBaseViewModel(): BaseViewModel {
        return BaseViewModel()
    }
}
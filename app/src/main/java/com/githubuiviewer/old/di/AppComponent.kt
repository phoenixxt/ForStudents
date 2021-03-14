package com.githubuiviewer.old.di

import android.content.Context
import com.githubuiviewer.old.ui.navigator.BaseFragment
import com.githubuiviewer.old.ui.issueScreen.IssueFragment
import com.githubuiviewer.old.ui.projectScreen.contributors.ContributorsFragment
import com.githubuiviewer.old.ui.projectScreen.issues.BriefInfoIssuesFragment
import com.githubuiviewer.old.ui.projectScreen.readme.ReadMeFragment
import com.githubuiviewer.old.ui.updateTokenFragment.UpdateTokenFragment
import com.githubuiviewer.old.ui.userScreen.UserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [AppModule::class])
interface AppComponent {
    val context : Context

    fun inject(fragment: UserFragment)

    fun inject(fragment: UpdateTokenFragment)

    fun inject(fragment: BaseFragment)

    fun inject(fragment: IssueFragment)

    fun inject(fragment: ReadMeFragment)
  
    fun inject(fragment: BriefInfoIssuesFragment)
  
    fun inject(fragment: ContributorsFragment)
  
}
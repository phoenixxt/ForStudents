package com.githubuiviewer.old.ui.projectScreen.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.githubuiviewer.old.ui.navigator.BaseFragment
import com.githubuiviewer.old.ui.projectScreen.Page
import com.githubuiviewer.old.ui.projectScreen.UserAndRepoName
import com.githubuiviewer.old.ui.projectScreen.contributors.ContributorsFragment
import com.githubuiviewer.old.ui.projectScreen.issues.BriefInfoIssuesFragment
import com.githubuiviewer.old.ui.projectScreen.readme.ReadMeFragment

class DetailInfoReposAdapter(
    fragment: BaseFragment,
    private val userAndRepoName: UserAndRepoName
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = Page.values().size

    override fun createFragment(position: Int): Fragment {
        return when (Page.values()[position]) {
            Page.README -> {
                ReadMeFragment.newInstance(userAndRepoName)
            }
            Page.CONTRIBUTORS -> {
                ContributorsFragment.newInstance(userAndRepoName)
            }
            Page.ISSUES -> {
                BriefInfoIssuesFragment.newInstance(userAndRepoName)
            }
        }
    }

    @StringRes
    fun getItemTabName(position: Int): Int {
        return Page.values()[position].titlePage
    }
}


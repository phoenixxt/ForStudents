package com.githubuiviewer.newpackage.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubuiviewer.R
import com.githubuiviewer.newpackage.LinearUiPagination
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment(R.layout.fragment_user) {

    lateinit var userViewModel: UserViewModel

    private val repoAdapter = RepoAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        reposRecyclerView.adapter = repoAdapter
        reposRecyclerView.layoutManager = linearLayoutManager

        LinearUiPagination(
            reposRecyclerView,
            userViewModel::loadMoreRepos
        )
    }
}

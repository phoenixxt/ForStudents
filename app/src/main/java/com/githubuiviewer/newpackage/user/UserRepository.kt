package com.githubuiviewer.newpackage.user

import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.newpackage.Pagination
import com.githubuiviewer.old.datasource.model.ReposResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class UserRepository(
    private val service: GitHubService,
    pagination: Pagination
) {
    private val reposSharedFlow = MutableSharedFlow<List<ReposResponse>>(replay = 1)

    private val reposPagination = pagination.counterPagination {
        service.getReposByToken(PER_PAGE, it)
    }

    fun flowRepos(): Flow<List<ReposResponse>> {
        return reposSharedFlow
    }

    suspend fun loadMore() {
        try {
            val newElements = reposPagination.load()
            val oldList = reposSharedFlow.replayCache.firstOrNull() ?: emptyList()
            reposSharedFlow.emit(oldList + newElements)
        } catch (exception: Exception) {

        }
    }

    private companion object {
        const val PER_PAGE = 100
    }
}
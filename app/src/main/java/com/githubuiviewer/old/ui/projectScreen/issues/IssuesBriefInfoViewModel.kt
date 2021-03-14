package com.githubuiviewer.old.ui.projectScreen.issues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubuiviewer.old.datasource.api.DataLoadingException
import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.old.datasource.api.NetworkException
import com.githubuiviewer.old.datasource.api.UnauthorizedException
import com.githubuiviewer.old.datasource.model.IssueResponse
import com.githubuiviewer.old.tools.PER_PAGE
import com.githubuiviewer.old.tools.State
import com.githubuiviewer.old.ui.BaseViewModel
import com.githubuiviewer.old.ui.projectScreen.UserAndRepoName
import com.githubuiviewer.old.tools.PagingDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssuesBriefInfoViewModel
@Inject constructor(
    private val gitHubService: GitHubService
) : BaseViewModel() {

    private val _issuesLiveData =
        MutableLiveData<State<PagingData<IssueResponse>, Exception>>()
    val issuesLiveData
        get() = _issuesLiveData as LiveData<State<PagingData<IssueResponse>, Exception>>

    val viewModelScope = this.baseViewModelScope

    fun getIssues(userAndRepoName: UserAndRepoName) {
        baseViewModelScope.launch {
            _issuesLiveData.postValue(State.Loading)
            reposFlow(
                userAndRepoName.userName,
                userAndRepoName.repoName
            ).collectLatest { pagedData ->
                _issuesLiveData.postValue(State.Content(pagedData))
            }
        }
    }

    private suspend fun reposFlow(owner: String, repoName: String): Flow<PagingData<IssueResponse>> {
        return Pager(PagingConfig(PER_PAGE)) {
            PagingDataSource(baseViewModelScope) { currentPage ->
                gitHubService.getIssues(owner, repoName, PER_PAGE, currentPage)
            }
        }.flow.cachedIn(baseViewModelScope)
    }

    override fun unauthorizedException() {
        super.unauthorizedException()
        _issuesLiveData.postValue(State.Error(UnauthorizedException()))
    }

    override fun dataLoadingException() {
        super.dataLoadingException()
        _issuesLiveData.postValue(State.Error(DataLoadingException()))
    }

    override fun networkException() {
        super.networkException()
        _issuesLiveData.postValue(State.Error(NetworkException()))
    }
}
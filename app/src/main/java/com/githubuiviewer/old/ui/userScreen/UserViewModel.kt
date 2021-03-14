package com.githubuiviewer.old.ui.userScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubuiviewer.old.data.repository.GitHubRepository
import com.githubuiviewer.old.datasource.api.DataLoadingException
import com.githubuiviewer.newpackage.GitHubService
import com.githubuiviewer.old.datasource.api.NetworkException
import com.githubuiviewer.old.datasource.api.UnauthorizedException
import com.githubuiviewer.old.datasource.model.ReposResponse
import com.githubuiviewer.old.datasource.model.UserResponse
import com.githubuiviewer.old.tools.PER_PAGE
import com.githubuiviewer.old.tools.State
import com.githubuiviewer.old.tools.UserProfile
import com.githubuiviewer.old.ui.BaseViewModel
import com.githubuiviewer.old.tools.PagingDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val gitHubService: GitHubService
) : BaseViewModel() {
    lateinit var userProfile: UserProfile

    private val _userInfoLiveData = MutableLiveData<State<UserResponse, Exception>>()
    val userInfoLiveData: LiveData<State<UserResponse, Exception>> = _userInfoLiveData

    private val _reposLiveData = MutableLiveData<PagingData<ReposResponse>>()
    val reposLiveData: LiveData<PagingData<ReposResponse>> = _reposLiveData

    private val _searchLiveData = MutableLiveData<PagingData<UserResponse>>()
    val searchLiveData: LiveData<PagingData<UserResponse>> = _searchLiveData

    val baseScope = baseViewModelScope
    lateinit var currentUserName: String

    fun getContent() {
        _userInfoLiveData.value = State.Loading
        baseViewModelScope.launch {
            val user = gitHubRepository.getUser(userProfile)
            currentUserName = user.name
            _userInfoLiveData.postValue(State.Content(user))
            reposFlow().collectLatest { pagedData ->
                _reposLiveData.postValue(pagedData)
            }
        }
    }

    private suspend fun reposFlow(): Flow<PagingData<ReposResponse>> {
        return Pager(PagingConfig(PER_PAGE)) {
            PagingDataSource(baseViewModelScope) { currentPage ->
                gitHubRepository.getRepos(userProfile, currentPage)
            }
        }.flow.cachedIn(baseViewModelScope)
    }

    fun getSearchable(query: String) {
        baseViewModelScope.launch {
            searchFlow(query).collectLatest { pagedData ->
                _searchLiveData.postValue(pagedData)
            }
        }
    }

    private suspend fun searchFlow(query: String): Flow<PagingData<UserResponse>> {
        return Pager(PagingConfig(PER_PAGE)) {
            PagingDataSource(baseViewModelScope) { currentPage ->
                gitHubService.getSearcher(query, PER_PAGE, currentPage).items
            }
        }.flow.cachedIn(baseViewModelScope)
    }

    override fun unauthorizedException() {
        super.unauthorizedException()
        _userInfoLiveData.postValue(State.Error(UnauthorizedException()))
    }

    override fun networkException() {
        super.networkException()
        _userInfoLiveData.postValue(State.Error(NetworkException()))
    }

    override fun dataLoadingException() {
        super.dataLoadingException()
        _userInfoLiveData.postValue(State.Error(DataLoadingException()))
    }
}
package com.githubuiviewer.old.ui.projectScreen.readme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubuiviewer.old.data.repository.GitHubRepository
import com.githubuiviewer.old.datasource.api.DataLoadingException
import com.githubuiviewer.old.datasource.api.NetworkException
import com.githubuiviewer.old.datasource.api.UnauthorizedException
import com.githubuiviewer.old.tools.State
import com.githubuiviewer.old.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class ReadMeViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : BaseViewModel() {

    private val _readMeLiveData = MutableLiveData<State<String, IOException>>()
    val readMeLiveData
        get() = _readMeLiveData as LiveData<State<String, IOException>>

    fun getReadme(owner: String, repoName: String) {
        baseViewModelScope.launch {
            _readMeLiveData.postValue(State.Loading)
            val readMe = gitHubRepository.getReadMe(owner, repoName)
            _readMeLiveData.postValue(State.Content(readMe))
        }
    }

    override fun unauthorizedException() {
        super.unauthorizedException()
        _readMeLiveData.postValue(State.Error(UnauthorizedException()))
    }

    override fun dataLoadingException() {
        super.dataLoadingException()
        _readMeLiveData.postValue(State.Error(DataLoadingException()))
    }

    override fun networkException() {
        super.networkException()
        _readMeLiveData.postValue(State.Error(NetworkException()))
    }
}
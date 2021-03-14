package com.githubuiviewer.old.ui.issueScreen

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
import com.githubuiviewer.old.datasource.model.IssueCommentRepos
import com.githubuiviewer.old.datasource.model.ReactionContent
import com.githubuiviewer.old.tools.Emoji
import com.githubuiviewer.old.tools.PER_PAGE
import com.githubuiviewer.old.tools.State
import com.githubuiviewer.old.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import com.githubuiviewer.old.tools.PagingDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class IssueViewModel @Inject constructor(
    private val gitHubService: GitHubService,
    private val gitHubRepository: GitHubRepository
) : BaseViewModel() {

    val baseScope = baseViewModelScope
    lateinit var issuesDetailsParameter: IssuesDetailsParameter

    private val _commentLiveData = MutableLiveData<State<PagingData<IssueCommentRepos>, IOException>>()
    val commentLiveData
        get() = _commentLiveData as LiveData<State<PagingData<IssueCommentRepos>, IOException>>

    fun getContent() {
        baseViewModelScope.launch {
            commentsFlow().collectLatest { pagedData ->
                _commentLiveData.postValue(State.Content(pagedData))
            }
        }
    }

    fun createReaction(reaction: Emoji, issueCommentRepos: IssueCommentRepos) {
        baseViewModelScope.launch(Dispatchers.IO) {
            if (issueCommentRepos.id == issuesDetailsParameter.issue_number) {
                gitHubService.createReactionForIssue(
                    owner = issuesDetailsParameter.owner,
                    repo = issuesDetailsParameter.repo,
                    issue_number = issuesDetailsParameter.issue_number,
                    content = ReactionContent(reaction.githubReaction)
                )
            } else {
                gitHubService.createReactionForIssueComment(
                    owner = issuesDetailsParameter.owner,
                    repo = issuesDetailsParameter.repo,
                    comment_id = issueCommentRepos.id,
                    content = ReactionContent(reaction.githubReaction)
                )
            }
            getContent()
        }
    }

    private suspend fun commentsFlow(): Flow<PagingData<IssueCommentRepos>> {
        return Pager(PagingConfig(PER_PAGE)) {
            PagingDataSource(baseViewModelScope) { currentPage ->
                gitHubRepository.getIssueComment(issuesDetailsParameter, currentPage)
            }
        }.flow.cachedIn(baseViewModelScope)
    }

    override fun unauthorizedException() {
        super.unauthorizedException()
        _commentLiveData.postValue(State.Error(UnauthorizedException()))
    }

    override fun networkException() {
        super.networkException()
        _commentLiveData.postValue(State.Error(NetworkException()))
    }

    override fun dataLoadingException() {
        super.dataLoadingException()
        _commentLiveData.postValue(State.Error(DataLoadingException()))
    }
}
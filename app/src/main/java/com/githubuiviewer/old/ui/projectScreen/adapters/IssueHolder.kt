package com.githubuiviewer.old.ui.projectScreen.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.githubuiviewer.databinding.IssueBriefInfoItemBinding
import com.githubuiviewer.old.datasource.model.IssueResponse

class IssueHolder(
    view: View, private var callback: (IssueResponse) -> Unit = { }
) : RecyclerView.ViewHolder(view) {
    private lateinit var binding: IssueBriefInfoItemBinding

    fun onBind(issueResponse: IssueResponse) {
        binding = IssueBriefInfoItemBinding.bind(itemView)
        binding.apply {
            root.setOnClickListener {
                callback(issueResponse)
            }
            tvIssueBriefInfo.text = issueResponse.title
        }
    }
}
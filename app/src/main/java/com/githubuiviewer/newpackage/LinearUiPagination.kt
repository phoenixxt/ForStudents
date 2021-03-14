package com.githubuiviewer.newpackage

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearUiPagination(
    reposRecyclerView: RecyclerView,
    loadMore: () -> Unit,
    elementsCountBeforeLoadMore: Int = 10
) {

    private var isLoading = false

    init {
        val linearLayoutManager = (reposRecyclerView.layoutManager as? LinearLayoutManager)
            ?: throw IllegalStateException("LinearUiPagination works only with LinearLayoutManager")

        reposRecyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                isLoading = false
            }
        })

        reposRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (isLoading) {
                    return
                }

                val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()

                val itemsCount = recyclerView.adapter?.itemCount ?: return

                if (lastVisiblePosition > itemsCount - elementsCountBeforeLoadMore) {
                    isLoading = true
                    loadMore()
                }
            }
        })
    }

}

package com.githubuiviewer.newpackage

import java.util.concurrent.atomic.AtomicInteger

class PageCounterPagination<T>(
    startingPage: Int,
    private val getRequest: suspend (Int) -> T
) {
    private val counter = AtomicInteger(startingPage)

    suspend fun load(): T {
        val result = getRequest(counter.get())
        counter.incrementAndGet()
        return result
    }
}

class Pagination {
    fun <T> counterPagination(startingPage: Int = 0, getRequest: suspend (Int) -> T): PageCounterPagination<T> {
        return PageCounterPagination(startingPage, getRequest)
    }
}
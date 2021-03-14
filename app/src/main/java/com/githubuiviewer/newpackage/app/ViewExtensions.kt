package com.githubuiviewer.newpackage.app

import android.view.View

private var lastClickTime = 0L
private const val CLICK_TIME_DIFFERENCE = 300

fun View.setSafeOnClickListener(doOnClick: () -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - CLICK_TIME_DIFFERENCE > lastClickTime) {
            lastClickTime = System.currentTimeMillis()
            doOnClick()
        }
    }
}

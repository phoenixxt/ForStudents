package com.githubuiviewer.newpackage.app

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.githubuiviewer.R
import com.githubuiviewer.old.ui.loginScreen.LoginFragment
import com.githubuiviewer.old.ui.updateTokenFragment.UpdateTokenFragment
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object Navigator {
    private var activity: AppCompatActivity? = null

    private val queue: MutableList<() -> Unit> = Collections.synchronizedList(mutableListOf())

    private val isOnResume = AtomicBoolean(false)

    fun provideActivity(activity: AppCompatActivity) {
        this.activity = activity

        addLifecycleEventObserver()
    }

    fun openLogin() {
        doOrPutInQueue {
            doInTransaction {
                replace(R.id.fragmentHolder, LoginFragment.newInstance())
            }
        }
    }

    fun openUpdateTokenFragment(code: String) {
        doOrPutInQueue {
            doInTransaction {
                replace(R.id.fragmentHolder, UpdateTokenFragment.newInstance(code))
            }
        }
    }

    fun openCurrentUserScreen() {


    }

    private fun addLifecycleEventObserver() {
        var observer: LifecycleEventObserver? = null
        observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    isOnResume.set(false)
                }
                Lifecycle.Event.ON_RESUME -> {
                    isOnResume.set(true)
                    queue.forEach { it.invoke() }
                    queue.clear()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    observer?.let { activity?.lifecycle?.removeObserver(it) }
                    activity = null
                }
            }
        }
        activity?.lifecycle?.addObserver(observer)
    }

    private fun doOrPutInQueue(action: () -> Unit) {
        if (isOnResume.get()) {
            action()
        } else {
            queue.add(action)
        }
    }

    private fun doInTransaction(action: FragmentTransaction.() -> Unit) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.apply(action)
            ?.commit()
    }
}

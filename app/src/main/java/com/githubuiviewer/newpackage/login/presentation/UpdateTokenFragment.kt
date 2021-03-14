package com.githubuiviewer.newpackage.login.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.githubuiviewer.R
import com.githubuiviewer.newpackage.app.setSafeOnClickListener
import com.githubuiviewer.newpackage.login.LoginComponent
import com.githubuiviewer.newpackage.login.data.LoginRepository
import com.githubuiviewer.old.tools.FragmentArgsDelegate
import kotlinx.android.synthetic.main.fragment_update_token.*
import kotlinx.coroutines.launch

class UpdateTokenFragment : Fragment(R.layout.fragment_update_token) {

    private var code: String by FragmentArgsDelegate()

    private val loginRepository = LoginComponent.createLoginRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorTextView.setSafeOnClickListener {
            updateToken()
        }

        updateToken()
    }

    private fun updateToken() {
        changeViewState(false)
        lifecycleScope.launch {
            try {
                loginRepository.updateToken(code)
            } catch (e: Exception) {
                changeViewState(true)
            }
        }
    }

    private fun changeViewState(isError: Boolean) {
        progressBar.isVisible = !isError
        errorTextView.isVisible = isError
    }

    fun newInstance(code: String): UpdateTokenFragment {
        return UpdateTokenFragment()
            .apply {
                this.code = code
            }
    }
}

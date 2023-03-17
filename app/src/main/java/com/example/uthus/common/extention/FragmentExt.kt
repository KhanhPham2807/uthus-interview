package com.example.uthus.common.extention

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object FragmentExt {
    fun <T : Any, S : Flow<T?>> Fragment.collectFlowWhenStarted(stateFlow: S, body: (T) -> Unit) {
        this.viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateFlow.collect { data ->
                    data?.let {
                        body.invoke(it)

                    }
                }
            }
        }
    }
}
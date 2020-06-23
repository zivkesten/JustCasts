package com.zk.justcasts.main.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zk.justcasts.R
import com.zk.justcasts.main.model.Event
import com.zk.justcasts.main.model.ViewEffect

class MainActivityViewModel: ViewModel() {

    companion object {
        private val TAG = MainActivityViewModel::class.java.simpleName
    }

    private val viewAction = MutableLiveData<ViewEffect>()

    val obtainViewEffects: LiveData<ViewEffect> = viewAction


    fun event(event: Event) {
        when (event) {
            is Event.Navigation -> {
                val visibility: Int =
                    when (event.destinationId) {
                        R.id.showFragment -> {
                            View.GONE
                        }
                        else -> {
                            View.VISIBLE
                        }
                    }
                viewAction.postValue(ViewEffect.AnimateNavigationViewVisibility(visibility = visibility))

            }
        }
    }
}
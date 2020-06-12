package com.zk.justcasts.main.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
            is Event.ScreenLoad -> Log.d(TAG, "ViewModel, screen load event")
//            is Event.TransitionToDetailFragment -> {
//                viewAction.postValue(ViewEffect.TransitionToScreenWithElement(event.data))
//            }
        }
    }
}
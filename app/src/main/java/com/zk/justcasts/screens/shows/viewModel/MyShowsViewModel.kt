package com.zk.justcasts.screens.shows.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyShowsViewModel(private val repository: Repository): ViewModel() {

    private val viewState = MutableLiveData<ViewState>()

    private val viewAction = MutableLiveData<ViewEffect>()

    val obtainState: LiveData<ViewState> = viewState

    val obtainViewEffects: LiveData<ViewEffect> = viewAction

    fun event(event: Event) {
        when(event) {
            is Event.ScreenLoad, Event.SwipeToRefreshEvent -> getPodcasts()
            is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreenWithElement)
        }
    }

    private fun getPodcasts() {
        viewModelScope.launch {
            val response = loadPodcastsFromApi()
            response?.podcasts?.let {
                val state = ViewState(it)
                viewState.postValue(state)
            }
        }
    }

    private suspend fun loadPodcastsFromApi() =
        withContext(Dispatchers.IO) {
            repository.getPodcasts()
        }
}
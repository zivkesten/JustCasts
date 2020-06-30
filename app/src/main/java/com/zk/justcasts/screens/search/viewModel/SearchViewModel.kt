package com.zk.justcasts.screens.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.search.model.Event
import com.zk.justcasts.screens.search.model.Result
import com.zk.justcasts.screens.search.model.ViewEffect
import com.zk.justcasts.screens.search.model.ViewState
import kotlinx.coroutines.launch

class SearchViewModel(val repository: Repository): ViewModel() {
    private val viewStateLD = MutableLiveData<ViewState>()
    private val viewEffectLD = MutableLiveData<ViewEffect>()

    val viewState: LiveData<ViewState> get() = viewStateLD
    val viewEffects: LiveData<ViewEffect> get() = viewEffectLD

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewStateLD.value = value
        }

    fun onEvent(event: Event) {
        Log.d("Zivi","----- event ${event.javaClass.simpleName}")
        eventToResult(event)
    }

    private fun eventToResult(event: Event) {
        when (event) {
            is Event.ScreenLoad -> { onScreenLoad() }
            is Event.SearchTextInput -> { onSearchTextInput(event.text) }
        }
    }

    private fun onScreenLoad() {
        resultToViewEffect(Lce.Loading())
        //resultToViewState(Lce.Loading())
        viewModelScope.launch {
            val podcastsResponse = repository.getPodcastsASync()
            resultToViewState(Lce.Content(Result.SearchResults(podcastsResponse.podcasts)))
        }
    }

    private fun onSearchTextInput(text: String) {
        resultToViewEffect(Lce.Loading())
        resultToViewState(Lce.Loading())
        viewModelScope.launch {
            val results = repository.search(text)
            results?.let {
                resultToViewState(Lce.Content(Result.SearchResults(it)))
            }
        }
    }

    // -----------------------------------------------------------------------------------
    // Internal helpers

    private fun resultToViewState(result: Lce<Result>) {
        Log.d("Zivi", "----- result $result")

        currentViewState = when (result) {
            is Lce.Content -> {
                when (result.packet) {
                    is Result.SearchResults ->
                        currentViewState.copy(
                            searchResultList = result.packet.searchResults)
                    else -> currentViewState.copy()
                }
            }

            is Lce.Loading -> {
                currentViewState.copy(/*loading state*/)
            }

            is Lce.Error -> {
                currentViewState.copy(/*error state with 'it'*/)
            }
        }
    }

    private fun resultToViewEffect(result: Lce<Result>){
        var effect: ViewEffect? = ViewEffect.NoEffect
        when (result) {
            is Lce.Content -> {
                when (result.packet)  {
                    is Result.SearchTextInputResult -> effect = ViewEffect.NoEffect
                }
            }
        }
        Log.d("Zivi", "resultToViewEffect $effect")
        viewEffectLD.value = effect
    }
}
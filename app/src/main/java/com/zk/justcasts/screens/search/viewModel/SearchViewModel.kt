package com.zk.justcasts.screens.search.viewModel

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseViewModel
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.search.model.Event
import com.zk.justcasts.screens.search.model.Result
import com.zk.justcasts.screens.search.model.ViewEffect
import com.zk.justcasts.screens.search.model.ViewState
import com.zk.justcasts.screens.shows.views.MyShowsFragmentDirections
import kotlinx.coroutines.launch

class SearchViewModel(val repository: Repository)
    : BaseViewModel<ViewState, ViewEffect, Event, Result>(ViewState()){

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewStateLD.value = value
        }

    override fun eventToResult(event: Event) {
        when (event) {
            is Event.ScreenLoad -> { onScreenLoad() }
            is Event.SearchTextInput -> { onSearchTextInput(event.text) }
            is Event.ItemClicked -> { onItemClick(event.item, event.sharedElement) }
        }
    }

    private fun onItemClick(item: PodcastDTO, sharedElement: View) {
        val result = Result.ItemClickedResult(item, sharedElement)
        val lceOfResult: Lce.Content<Result> = Lce.Content(result)
        resultToViewEffect(lceOfResult)
    }

    private fun onScreenLoad() {
        resultToViewState(Lce.Loading())
        viewModelScope.launch {
            val podcastsResponse = repository.getPodcastsASync()
            resultToViewState(Lce.Content(Result.SearchResults(podcastsResponse.podcasts)))
        }
    }

    private fun onSearchTextInput(text: String) {
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

    override fun resultToViewState(result: Lce<Result>) {
        Log.d("zorg", "----- result $result")

        currentViewState = when (result) {
            is Lce.Content -> {
                when (result.packet) {
                    is Result.SearchResults ->
                        currentViewState.copy(
                            searchResultList = result.packet.searchResults,
                            loadingStateVisibility = View.GONE)
                    else -> currentViewState.copy()
                }
            }

            is Lce.Loading -> {
                currentViewState.copy(loadingStateVisibility = View.VISIBLE)
            }

            is Lce.Error -> {
                currentViewState.copy(
                    loadingStateVisibility = View.GONE)
            }
        }
    }

    override fun resultToViewEffect(result: Lce<Result>){
        var effect: ViewEffect? = null
        when (result) {
            is Lce.Content -> {
                effect = when (result.packet)  {
                    is Result.SearchResults, Result.ScreeLoad -> ViewEffect.NoEffect
                    is Result.ItemClickedResult -> itemClickToViewEffect(result.packet)
                }
            }
            else -> Unit// TODO: Handle
        }
        Log.d("Zivi", "resultToViewEffect $effect")
        viewEffectLD.value = effect
    }

    private fun itemClickToViewEffect(it: Result.ItemClickedResult): ViewEffect.TransitionToScreenWithElement? {
        var directions: ViewEffect.TransitionToScreenWithElement? = null
        val sharedElement = it.sharedElement
        val item = it.item
        ViewCompat.getTransitionName(sharedElement)?.let { transitionName ->
            val extras = FragmentNavigatorExtras(sharedElement to transitionName)
            val direction = MyShowsFragmentDirections.selectShow(item, transitionName)
            directions = ViewEffect.TransitionToScreenWithElement(extras, direction)
        }
        return directions
    }
}
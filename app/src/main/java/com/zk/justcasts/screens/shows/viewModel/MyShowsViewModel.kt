package com.zk.justcasts.screens.shows.viewModel

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseViewModel
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.repository.database.ShowsDatabase
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.Result
import com.zk.justcasts.screens.shows.model.Result.ItemClickedResult
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.views.MyShowsFragmentDirections
import kotlinx.coroutines.launch

class MyShowsViewModel(private val dataBase: ShowsDatabase):
    BaseViewModel<ViewState, ViewEffect, Event, Result>(ViewState()) {

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewStateLD.value = value
        }

    override fun eventToResult(event: Event) {
        when (event) {
            is Event.SwipeToRefreshEvent, Event.ScreenLoad -> { loadFromCache() }
            is Event.ItemClicked -> { onItemClick(event.item, event.SharedElement) }
        }
    }

    private fun loadFromCache() {
        resultToViewState(Lce.Loading())
        if (loadFromBEJob?.isActive == true) loadFromBEJob?.cancel()

        loadFromBEJob = viewModelScope.launch {
            val myShows = dataBase.podcastDao().getAll()
            val result: Lce<Result> = if (myShows.isEmpty()) {
                Lce.Error(Result.GetPodcastsResult(myShows))
            } else {
                Lce.Content(Result.GetPodcastsResult(myShows))
            }
            resultToViewState(result)
        }
    }

    private fun onItemClick(item: PodcastDTO, sharedElement: View) {
        val result = ItemClickedResult(item, sharedElement)
        val lceOfResult: Lce.Content<Result> = Lce.Content(result)
        resultToViewEffect(lceOfResult)
        resultToViewState(lceOfResult)
    }

    // -----------------------------------------------------------------------------------
    // Internal helpers

    override fun resultToViewState(result: Lce<Result>) {
        Log.d("Zivi", "----- result $result")

        currentViewState = when (result) {
            is Lce.Loading -> currentViewState.copy(/*loading state*/)
            is Lce.Error -> currentViewState.copy(/*error state with 'it'*/)
            is Lce.Content -> {
                when (result.packet) {
                    is Result.ScreenLoadResult ->  currentViewState.copy()
                    is ItemClickedResult -> currentViewState.copy()
                    is Result.GetPodcastsResult -> {
                        val podcasts = result.packet.podcastsResponse
                        currentViewState.copy(itemList = podcasts.map { it.dto()})
                    }
                }
            }
        }
    }

    override fun resultToViewEffect(result: Lce<Result>){
        var effect: ViewEffect? = ViewEffect.NoEffect
        when (result) {
            is Lce.Content -> {
                when (result.packet)  {
                    is ItemClickedResult -> effect = itemClickToViewEffect(result.packet)
                }
            }
        }
        viewEffectLD.value = effect
    }

    private fun itemClickToViewEffect(it: ItemClickedResult): ViewEffect.TransitionToScreenWithElement? {
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
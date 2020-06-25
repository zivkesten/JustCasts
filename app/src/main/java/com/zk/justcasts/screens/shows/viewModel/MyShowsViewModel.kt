package com.zk.justcasts.screens.shows.viewModel

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.Result
import com.zk.justcasts.screens.shows.model.Result.ItemClickedResult
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.views.MyShowsFragmentDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyShowsViewModel(private val repository: Repository): ViewModel() {

    private val viewStateLD = MutableLiveData<ViewState>()
    private val viewEffectLD = MutableLiveData<ViewEffect>()
    val viewState: LiveData<ViewState> get() = viewStateLD
    val viewEffects: LiveData<ViewEffect> get() = viewEffectLD

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewStateLD.value = value
        }

    private var loadFromBEJob: Job? = null

    fun onEvent(event: Event) {
        Log.d("Zivi","----- event ${event.javaClass.simpleName}")
        eventToResult(event)
    }

    private fun eventToResult(event: Event) {
        when (event) {
            is Event.SwipeToRefreshEvent, Event.ScreenLoad -> { loadFromApi() }
            is Event.ItemClicked -> { onItemClick(event.item, event.SharedElement) }
        }
    }

    private fun loadFromApi() {
        resultToViewState(Lce.Loading())
        if (loadFromBEJob?.isActive == true) loadFromBEJob?.cancel()

        loadFromBEJob = viewModelScope.launch {
            val response = repository.getPodcastsASync()
            val result: Lce<Result> = if (response.errorMessage?.isEmpty() == false) {
                Lce.Error(Result.GetPodcastsResult(response))
            } else {
                Lce.Content(Result.GetPodcastsResult(response))
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

    private fun resultToViewState(result: Lce<Result>) {
        Log.d("Zivi", "----- result $result")

        currentViewState = when (result) {
            is Lce.Content -> {
                when (result.packet) {
                    is Result.ScreenLoadResult -> {
                        currentViewState.copy()
                    }
                    is Result.GetPodcastsResult -> {
                        val podcasts = result.packet.podcastsResponse.podcasts
                        currentViewState.copy(
                            itemList = podcasts
                        )
                    }

                    is ItemClickedResult -> {
                        currentViewState.copy()
                    }
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
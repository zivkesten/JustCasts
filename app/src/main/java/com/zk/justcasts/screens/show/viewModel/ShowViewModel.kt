package com.zk.justcasts.screens.show.viewModel

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.R
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseViewModel
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.repository.database.ShowsDatabase
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.Result
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import com.zk.justcasts.screens.show.views.ShowFragmentDirections
import kotlinx.coroutines.launch

class ShowViewModel(private val database: ShowsDatabase, private val repository: Repository) :
    BaseViewModel<ViewState, ViewEffect, Event, Result>(ViewState()){

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewStateLD.value = value
        }

    override fun eventToResult(event: Event) {
        when (event) {
            is Event.ScreenLoad -> { onScreenLoad(event.data) }
            is Event.AddToMyShows -> { onAddToMyShows(event.item) }
            is Event.ListItemClicked -> { onItemClick(event.item, event.sharedElement) }
        }
    }

    private fun onScreenLoad(item: PodcastDTO) {
        resultToViewState(Lce.Loading())
        try {
        viewModelScope.launch {
            val episodes = repository.getEpisodesASync(item.id)
            Log.d("Zivi", "episodes: $episodes")
            resultToViewState(Lce.Content(Result.LoadEpisodes(episodes)))
        }
        } catch (e: Exception) {
            Log.e(ShowViewModel::class.java.simpleName, "Error loading users ${e.localizedMessage}")
            resultToViewState(Lce.Error(Result.Error(e.localizedMessage)))
        }
    }

    private fun onItemClick(item: EpisodeDTO, sharedElement: View) {
        val result = Result.ItemClickedResult(item, sharedElement)
        val lceOfResult: Lce.Content<Result> = Lce.Content(result)
        resultToViewEffect(lceOfResult)
        resultToViewState(lceOfResult)
    }

    private fun onAddToMyShows(item: PodcastDTO) {
        resultToViewState(Lce.Loading())
        viewModelScope.launch {
            val id = database.podcastDao().insert(item.entity())
            val result = Result.ShowAddToFavConfirmation(item)

            //Some error handling here?
            if (id > 0) {
                val lceOfResult: Lce.Content<Result> = Lce.Content(result)
                resultToViewEffect(lceOfResult)
                resultToViewState(lceOfResult)
            } else {
                val r = Result.Error("Error adding to dataBase")
                val lceOfResult: Lce.Error<Result> = Lce.Error(r)
                resultToViewEffect(lceOfResult)
                resultToViewState(lceOfResult)
            }
        }
    }

    // -----------------------------------------------------------------------------------
    // Internal helpers

    override fun resultToViewState(result: Lce<Result>) {
        Log.d("Zivi", "----- result $result")

        currentViewState = when (result) {
            is Lce.Loading -> currentViewState.copy(loadingStateVisibility = View.VISIBLE)
            is Lce.Error -> {
                when (result.packet) {
                    is Result.Error -> currentViewState.copy(errorMessage = result.packet.errorMessage)
                    else -> currentViewState.copy(errorMessageResource = R.string.unexpectedError)
                }
            }
            is Lce.Content -> {
                when (result.packet) {
                    is Result.LoadEpisodes -> currentViewState.copy(episodes = result.packet.episodes)
                    else -> currentViewState.copy()
                }
            }
        }
    }

    override fun resultToViewEffect(result: Lce<Result>){
        var effect: ViewEffect? = ViewEffect.NoEffect
        when (result) {
            is Lce.Content -> {
                when (result.packet)  {
                    is Result.ShowAddToFavConfirmation -> effect = ViewEffect.ShowAddToFavConfirmation(result.packet.podcastAdded)
                    is Result.ItemClickedResult -> effect = itemClickToViewEffect(result.packet)
                }
            }
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
            val direction = ShowFragmentDirections.selectEpisode(item, transitionName)
            directions = ViewEffect.TransitionToScreenWithElement(extras, direction)
        }
        return directions
    }
}
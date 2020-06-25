package com.zk.justcasts.screens.show.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.repository.database.ShowsDatabase
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.Result
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import kotlinx.coroutines.launch

class ShowViewModel(private val database: ShowsDatabase, private val repository: Repository) : ViewModel() {

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
            is Event.ScreenLoad -> { Log.d("Zivi", "Screen load ${javaClass.simpleName}") }
            is Event.AddToMyShows -> { onAddToMyShows(event.item) }
        }
    }

    private fun onAddToMyShows(item: PodcastDTO) {
        resultToViewEffect(Lce.Loading())
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
                val lceOfResult: Lce.Error<Result> = Lce.Error(result)
                resultToViewEffect(lceOfResult)
                resultToViewState(lceOfResult)
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
                    is Result.ScreenLoad -> currentViewState.copy()
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
                    is Result.ShowAddToFavConfirmation -> effect = ViewEffect.ShowAddToFavConfirmation(result.packet.podcastAdded)
                }
            }
        }
        Log.d("Zivi", "resultToViewEffect $effect")
        viewEffectLD.value = effect
    }

//    private fun itemClickToViewEffect(it: Result.ItemClickedResult): com.zk.justcasts.screens.shows.model.ViewEffect.TransitionToScreenWithElement? {
//        var directions: com.zk.justcasts.screens.shows.model.ViewEffect.TransitionToScreenWithElement? = null
//        val sharedElement = it.sharedElement
//        val item = it.item
//        ViewCompat.getTransitionName(sharedElement)?.let { transitionName ->
//            val extras = FragmentNavigatorExtras(sharedElement to transitionName)
//            val direction = MyShowsFragmentDirections.selectShow(item, transitionName)
//            directions = com.zk.justcasts.screens.shows.model.ViewEffect.TransitionToScreenWithElement(extras, direction)
//        }
//        return directions
//    }


}
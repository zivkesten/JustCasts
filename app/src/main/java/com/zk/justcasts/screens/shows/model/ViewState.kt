package com.zk.justcasts.screens.shows.model

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.screens.shows.viewModel.BaseEvent
import com.zk.justcasts.screens.shows.viewModel.BaseResult
import com.zk.justcasts.screens.shows.viewModel.BaseViewEffect
import com.zk.justcasts.screens.shows.viewModel.BaseViewState


data class ViewState (
    val itemList: List<PodcastDTO> = emptyList()
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    object NoEffect: ViewEffect()
    data class TransitionToScreenWithElement(val extras: FragmentNavigator.Extras, val direction: NavDirections) : ViewEffect()
    data class ShowVisualResultForAddToFavourites(val message: String): ViewEffect()
}

sealed class Event: BaseEvent {
    object ScreenLoad:  Event()
    object SwipeToRefreshEvent: Event()
    data class ItemClicked(val item: PodcastDTO, val SharedElement: View): Event()
}

sealed class Result: BaseResult {
    object ScreenLoadResult : Result()
    data class GetPodcastsResult(val podcastsResponse: BestPodcastsResponse) : Result()
    data class ItemClickedResult(val item: PodcastDTO, val sharedElement: View) : Result()
}

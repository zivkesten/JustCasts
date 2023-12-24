package com.zk.justcasts.screens.shows.model

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseEvent
import com.zk.justcasts.presentation.base.BaseResult
import com.zk.justcasts.presentation.base.BaseViewEffect
import com.zk.justcasts.presentation.base.BaseViewState
import com.zk.justcasts.repository.database.podcast.PodcastEntity


data class ViewState (
    val itemList: List<PodcastDTO>? = null,
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE
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
    data class Error(val errorMessage: String?): Result()
    data class GetPodcastsResult(val podcastsResponse: List<PodcastEntity>) : Result()
    data class ItemClickedResult(val item: PodcastDTO, val sharedElement: View) : Result()
}

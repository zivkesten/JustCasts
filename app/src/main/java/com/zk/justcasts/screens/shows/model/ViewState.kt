package com.zk.justcasts.screens.shows.model

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.zk.justcasts.models.Podcast


data class ViewState(
    val itemList: List<Podcast>? = null
)

sealed class ViewEffect {
    object NoEffect: ViewEffect()
    data class TransitionToScreenWithElement(val extras: FragmentNavigator.Extras, val direction: NavDirections) : ViewEffect()
    data class ShowVisualResultForAddToFavourites(val message: String): ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    object SwipeToRefreshEvent: Event()
    object SubscribeToPodcast: Event()
    data class ItemClicked(val item: Podcast, val SharedElement: View): Event()
}

sealed class Result {
    object ScreenLoadResult : Result()
    data class GetPodcastsResult(val podcats: List<Podcast>) : Result()
    data class SubscribeResult(val success: Boolean) : Result()
    data class ItemClickedResult(val item: Podcast, val sharedElement: View) : Result()
}

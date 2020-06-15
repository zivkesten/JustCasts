package com.zk.justcasts.screens.shows.model

import com.zk.justcasts.models.Podcast


data class ViewState(
    val itemList: List<Podcast>? = null
)

sealed class ViewEffect {
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    object SwipeToRefreshEvent: Event()
    data class SubscribeToPodcast(val podcast: Podcast): Event()
}

sealed class Result {
    object ScreenLoadResult : Result()
    data class GetPodcastsResult(val podcats: List<Podcast>) : Result()
    data class SubscribeResult(val success: Boolean) : Result()
}

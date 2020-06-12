package com.zk.justcasts.shows.model

import com.zk.justcasts.models.Podcast


data class ViewState(
    val itemList: List<Podcast>
)

sealed class ViewEffect {
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    object SwipeToRefreshEvent: Event()
    data class DataReceived(val data: Any?) : Event()
    data class ListItemClicked(val item: Podcast): Event()
}

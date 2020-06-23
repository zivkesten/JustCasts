package com.zk.justcasts.screens.show.model

import com.zk.justcasts.models.Episode
import com.zk.justcasts.models.PodcastDTO

data class ViewState(val episodes: List<Episode>? = null,
                     val podcastTitle: String? = null,
                     val podcastImage: String? = null)

sealed class ViewEffect {
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    data class DataReceived(val data: Any?) : Event()
    data class AddToMyShows(val item: PodcastDTO): Event()
    data class ListItemClicked(val item: PodcastDTO): Event()
}

sealed class Result {
    object TransitionToScreenWithElement : Result()
    data class GetPodcastsResult(val podcasts: List<PodcastDTO>) : Result()
}

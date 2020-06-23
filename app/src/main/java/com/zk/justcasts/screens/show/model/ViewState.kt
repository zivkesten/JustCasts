package com.zk.justcasts.screens.show.model

import com.zk.justcasts.models.Episode
import com.zk.justcasts.models.Podcast

data class ViewState(val episodes: List<Episode>? = null,
                     val podcastTitle: String? = null,
                     val podcastImage: String? = null)

sealed class ViewEffect {
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    data class ListItemClicked(val item: Podcast): Event()
}

sealed class Result {
    object TransitionToScreenWithElement : Result()
    data class GetPodcastsResult(val podcasts: List<Podcast>) : Result()
}

package com.zk.justcasts.screens.show.model

import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO

data class ViewState(val episodes: List<EpisodeDTO>? = null,
                     val podcastTitle: String? = null,
                     val podcastImage: String? = null)

sealed class ViewEffect {
    object NoEffect : ViewEffect()
    object TransitionToScreenWithElement : ViewEffect()
    data class ShowAddToFavConfirmation(val podcastAdded: PodcastDTO) : ViewEffect()
}

sealed class Event {
    data class ScreenLoad(val data: PodcastDTO): Event()
    data class AddToMyShows(val item: PodcastDTO): Event()
    data class ListItemClicked(val item: PodcastDTO): Event()
}

sealed class Result {
    object TransitionToScreenWithElement : Result()
    object ScreenLoad:  Result()
    data class GetEpisodes(val episodes: List<EpisodeDTO>?): Result()
    data class ShowAddToFavConfirmation(val podcastAdded: PodcastDTO) : Result()
}

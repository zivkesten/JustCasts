package com.zk.justcasts.screens.search.model

import android.view.View
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO

data class ViewState(
    val searchResultList: List<PodcastDTO>? = emptyList(),
    val searchText: String? = null
)

sealed class ViewEffect {
    object NoEffect: ViewEffect()
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    data class SearchTextInput(val text: String) : Event()
    data class ItemClicked(val item: PodcastDTO, val sharedElement: View): Event()
}

sealed class Result {
   object ScreeLoad: Result()
   data class SearchTextInputResult(val text: String): Result()
   data class SearchResults(val searchResults: List<PodcastDTO>): Result()
}

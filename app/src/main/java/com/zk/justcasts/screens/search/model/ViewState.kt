package com.zk.justcasts.screens.search.model

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseEvent
import com.zk.justcasts.presentation.base.BaseResult
import com.zk.justcasts.presentation.base.BaseViewEffect
import com.zk.justcasts.presentation.base.BaseViewState

data class ViewState(
    val searchResultList: List<PodcastDTO>? = emptyList(),
    val searchText: String? = null,
    val loadingStateVisibility: Int? = View.GONE
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    object NoEffect: ViewEffect()
    data class TransitionToScreenWithElement(val extras: FragmentNavigator.Extras, val direction: NavDirections) : ViewEffect()
}

sealed class Event: BaseEvent {
    object ScreenLoad: Event()
    data class SearchTextInput(val text: String) : Event()
    data class ItemClicked(val item: PodcastDTO, val sharedElement: View): Event()
}

sealed class Result: BaseResult {
   object ScreeLoad: Result()
   data class SearchResults(val searchResults: List<PodcastDTO>): Result()
   data class ItemClickedResult(val item: PodcastDTO, val sharedElement: View) : Result()
}

package com.zk.justcasts.screens.show.model

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.base.BaseEvent
import com.zk.justcasts.presentation.base.BaseResult
import com.zk.justcasts.presentation.base.BaseViewEffect
import com.zk.justcasts.presentation.base.BaseViewState

data class ViewState(val episodes: List<EpisodeDTO>? = null,
                     val errorMessageResource: Int? = null,
                     val errorMessage: String? = null,
                     val loadingStateVisibility: Int? = View.GONE
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    object NoEffect : ViewEffect()
    data class TransitionToScreenWithElement(val extras: FragmentNavigator.Extras, val direction: NavDirections) : ViewEffect()
    data class ShowAddToFavConfirmation(val podcastAdded: PodcastDTO) : ViewEffect()
}

sealed class Event: BaseEvent {
    data class ScreenLoad(val data: PodcastDTO): Event()
    data class AddToMyShows(val item: PodcastDTO): Event()
    data class ListItemClicked(val item: EpisodeDTO, val sharedElement: View): Event()
}

sealed class Result: BaseResult {
    object ScreenLoad:  Result()
    data class ItemClickedResult(val item: EpisodeDTO, val sharedElement: View) : Result()
    data class LoadEpisodes(val episodes: List<EpisodeDTO>?): Result()
    data class ShowAddToFavConfirmation(val podcastAdded: PodcastDTO) : Result()
    data class Error(val errorMessage: String?): Result()
}

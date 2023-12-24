package com.zk.justcasts.main.model

data class ViewState(
    val name: String,
    val image: String,
    val phoneNumber: String,
    val email: String
)

sealed class ViewEffect {
    data class AnimateNavigationViewVisibility(val visibility: Int): ViewEffect()
}

sealed class Event {
    data class Navigation(val destinationId: Int):  Event()
    data class FragmentLoaded(val currentFragmentId: String): Event()
}

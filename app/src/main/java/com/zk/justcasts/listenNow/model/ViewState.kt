package com.zk.justcasts.listenNow.model

data class ViewState(
    val name: String,
    val image: String,
    val phoneNumber: String,
    val email: String
)

sealed class ViewEffect {
    object TransitionToScreenWithElement : ViewEffect()
}

sealed class Event {
    object ScreenLoad:  Event()
    data class DataReceived(val data: Any?) : Event()
}

package com.example.dogadoption.room.user

sealed interface UserEvent {
    object SaveUser: UserEvent
    data class SetUserName(val userName: String): UserEvent
    object ShowDialog: UserEvent
    object HideDialog: UserEvent

}
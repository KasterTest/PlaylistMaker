package com.bignerdranch.android.playlistmaker.playlist_creator.ui.models

sealed class ScreenState(
    val createButtonState: CreateButtonState = CreateButtonState.ENABLED
) {
    class Empty(
        createButtonState: CreateButtonState = CreateButtonState.DISABLED,
    ) : ScreenState(createButtonState)
    
    class HasContent(
        createButtonState: CreateButtonState = CreateButtonState.ENABLED,
    ) : ScreenState(createButtonState)
    
    object NeedsToAsk : ScreenState()
    
    object AllowedToGoOut : ScreenState()
}
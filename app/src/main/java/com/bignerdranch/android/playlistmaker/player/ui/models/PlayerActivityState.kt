package com.bignerdranch.android.playlistmaker.player.ui.models

sealed class PlayerActivityState {

    class StatePlayingPosition (var position : Int) : PlayerActivityState()
    object StatePlayerPlay : PlayerActivityState()
    object StatePlayerPause : PlayerActivityState()
    object StateOnComplitionTrack : PlayerActivityState()

}

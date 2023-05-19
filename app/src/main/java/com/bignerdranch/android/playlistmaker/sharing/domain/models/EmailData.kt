package com.bignerdranch.android.playlistmaker.sharing.domain.models

data class EmailData(
    val mail: String,
    val mailTo: String = "mailto:",
)

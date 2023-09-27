package com.bignerdranch.android.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun Int.millisConverter(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}
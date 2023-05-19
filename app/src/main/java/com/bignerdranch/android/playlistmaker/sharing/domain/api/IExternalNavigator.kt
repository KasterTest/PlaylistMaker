package com.bignerdranch.android.playlistmaker.sharing.domain.api

import com.bignerdranch.android.playlistmaker.sharing.domain.models.EmailData

interface IExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmail: EmailData)
}
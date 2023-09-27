package com.bignerdranch.android.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.sharing.domain.api.IExternalNavigator
import com.bignerdranch.android.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigator(private val context: Context) : IExternalNavigator {

    private val chooserTitle = context.getText(R.string.chooser_title)
    private val supportTitle = context.getString(R.string.settings_support_subject_message)
    private val supportMessage = context.getString(R.string.setting_supports_text_message)

    override fun shareLink(shareAppLink: String) {
        val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
        }, chooserTitle)

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(termsLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openEmail(supportEmail: EmailData) {

        val intent = Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(supportEmail.mailTo)
            putExtra(Intent.EXTRA_EMAIL, supportEmail.mail)
            putExtra(Intent.EXTRA_SUBJECT, supportTitle)
            putExtra(Intent.EXTRA_TEXT, supportMessage)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, chooserTitle)

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun share(text: String) {
        val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }, chooserTitle)

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
package com.bignerdranch.android.playlistmaker.playlist_menu.ui.bottom_sheet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetSetuper(private val activity: Activity?) {

    @SuppressLint("ResourceAsColor")
    fun setupRatio(bottomSheetDialog: BottomSheetDialog, percentage: Float) {

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomSheet.setBackgroundColor(Color.TRANSPARENT);
        val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(bottomSheet)

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getWindowHeight()
        bottomSheet.layoutParams = layoutParams
        val peekHeight = (layoutParams.height * percentage).toInt()
        behavior.peekHeight = peekHeight
        behavior.isFitToContents = false
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun setupRatio(
        container: LinearLayout,
        percentage: Float,
    ) {
        val bottomSheetBehavior = BottomSheetBehavior
            .from(container)
            .apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val screenHeight = getWindowHeight()
        val peekHeight = (screenHeight * percentage).toInt()
        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.isFitToContents = false

    }

    private fun getWindowHeight(): Int {

        val displayMetrics = DisplayMetrics()

        @Suppress("DEPRECATION") activity?.windowManager?.defaultDisplay?.getMetrics(
            displayMetrics
        )

        return displayMetrics.heightPixels
    }
}
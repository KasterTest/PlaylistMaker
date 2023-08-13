package com.bignerdranch.android.playlistmaker.playlist_creator.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PermissionResultState
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.models.CreateButtonState
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.models.ScreenState
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.view_model.NewPlaylistViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var isProcessingClick = false
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var binding: FragmentNewPlaylistBinding

    companion object {
        private const val QUALITY_IMAGE = 30
        private const val MESSAGE_DURATION = 4000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPickMediaRegister()
        initObserver()
        initListeners()
    }

    private fun initPickMediaRegister() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val cornerRadius = requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)
                binding.playlistCoverImage.setImage(it, cornerRadius)
                saveImageToPrivateStorage(it)
            }
        }
    }

    fun ImageView.setImage(uri: Uri, cornerRadius: Int) {
        Glide
            .with(this.context)
            .load(uri)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(this)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.screenStateFlow.collect { state ->
                when (state) {
                    is ScreenState.AllowedToGoOut -> goBack()
                    is ScreenState.Empty, is ScreenState.HasContent -> renderCreateBtn(state.createButtonState)
                    is ScreenState.NeedsToAsk -> showDialog()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.permissionStateFlow.collect { state ->
                onPlaylistCoverClickedCompleted()
                when (state) {

                    PermissionResultState.NEEDS_RATIONALE -> {
                        Toast.makeText(requireContext(), R.string.rationale_permission_message, Toast.LENGTH_SHORT).show()
                    }
                    PermissionResultState.GRANTED -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    PermissionResultState.DENIED_PERMANENTLY -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = Uri.fromParts("package", requireContext().packageName, null)
                        requireContext().startActivity(intent)
                    }
                }
            }
        }
    }

    private fun onPlaylistCoverClickedCompleted() {
        isProcessingClick = false
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            })



        binding.apply {
            navigationToolbar.setNavigationOnClickListener {
                viewModel.onBackPressed()
            }

            playlistCoverImage.setOnClickListener {
                if (isProcessingClick) {
                    return@setOnClickListener
                }
                isProcessingClick = true
                viewModel.onPlaylistCoverClicked()
            }

            playlistName.doOnTextChanged { text, _, _, _ ->
                renderBoxStrokeEditTextColor(binding.playlistNameContainer, text)
                viewModel.onPlaylistNameChanged(text.toString())
            }

            playlistDescription.doOnTextChanged { text, _, _, _ ->
                renderBoxStrokeEditTextColor(binding.playlistDescriptionContainer, text)
                viewModel.onPlaylistDescriptionChanged(text.toString())
            }

            buttonCreate.setOnClickListener {
                viewModel.onCreateBtnClicked()
                val message =
                    getString(R.string.playlist) + " \"" + playlistName.text.toString() + "\" " + getString(R.string.created)
                showMessage(message)
            }
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun renderCreateBtn(state: CreateButtonState) {
        binding.buttonCreate.isEnabled = state == CreateButtonState.ENABLED
    }

    private fun renderBoxStrokeEditTextColor(view: TextInputLayout, text: CharSequence?) {
        if (!text.isNullOrEmpty()) {
            view.defaultHintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.new_playlist_edit_text_blue)
            ContextCompat.getColorStateList(requireContext(), R.color.new_playlist_edit_text_blue)?.let { view.setBoxStrokeColorStateList(it) }
        } else {
            view.defaultHintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.new_playlist_edit_text_color)
            ContextCompat.getColorStateList(requireContext(), R.color.new_playlist_edit_text_color)?.let { view.setBoxStrokeColorStateList(it) }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_playlist_dialog)
            .setMessage(R.string.message_playlist_dialog)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.complete) { _, _ -> goBack() }
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), getString(R.string.my_playlists))
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment ?: "image")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, outputStream)

        viewModel.saveImageUri(file.toURI())
    }

    private fun showMessage(message: String) {
        val parentView = requireActivity().findViewById<View>(android.R.id.content)
        Snackbar
            .make(
                parentView,
                message,
                Snackbar.LENGTH_SHORT
            )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.bottom_snackbar_background))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_snackbar_text))
            .setDuration(MESSAGE_DURATION)
            .show()
    }

}
package com.example.memes.activities.add.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.memes.R
import com.example.memes.activities.add.Consts.PHOTO_URL

class ImageLoadDialogFragment : DialogFragment() {

    private lateinit var onImageChosenListener: ((String) -> Unit)
    private lateinit var loadFromGalleryButton: Button
    private lateinit var takePhotoButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_image_load_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFromGalleryButton = view.findViewById(R.id.loadFromGalleryButton)
        takePhotoButton = view.findViewById(R.id.takePhotoButton)

        loadFromGalleryButton.setOnClickListener { loadFromGallery() }
        takePhotoButton.setOnClickListener { takePhoto() }
    }

    fun setOnImageChosenListener(listener: (photoUrl: String) -> Unit) {
        onImageChosenListener = listener
    }

    private fun loadFromGallery() {
        onImageChosenListener(PHOTO_URL)
        dialog?.dismiss()
    }

    private fun takePhoto() {
        onImageChosenListener(PHOTO_URL)
        dialog?.dismiss()
    }
}
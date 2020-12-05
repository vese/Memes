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
import com.example.memes.activities.add.Consts


class ImageLoadDialogFragment : DialogFragment() {

    private var onImageChosenListener: ((String) -> Unit)? = null

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
        val loadFromGalleryButton = view.findViewById<Button>(R.id.loadFromGalleryButton)
        loadFromGalleryButton.setOnClickListener { _ -> loadFromGallery() }
        val takePhotoButton = view.findViewById<Button>(R.id.takePhotoButton)
        takePhotoButton.setOnClickListener { _ -> takePhoto() }
    }

    fun setOnImageChosenListener(listener: (photoUrl: String) -> Unit) {
        onImageChosenListener = listener
    }

    private fun loadFromGallery() {
        onImageChosenListener?.let { it(Consts.PHOTO_URL) }
        dialog?.dismiss()
    }

    private fun takePhoto() {
        onImageChosenListener?.let { it(Consts.PHOTO_URL) }
        dialog?.dismiss()
    }
}
package com.example.memes.activities.tabs.fragments.panel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memes.R

internal class PanelDataAdapter(
    private val context: Context,
    private val panels: List<Panel>,
    private val onClickListener: PanelDataAdapterListener
) :
    RecyclerView.Adapter<PanelDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cell, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val panel = panels[position]
        Glide.with(context).load(panel.photoUrl).into(holder.imageView)
        holder.textView.text = panel.title
        holder.favoriteButton.isSelected = panel.isFavorite
        holder.favoriteButton.setOnClickListener { v ->
            onClickListener.favoriteClickListener(v, panel)
        }
        holder.panelClickView.setOnClickListener { v ->
            onClickListener.panelClickListener(v, panel)
        }
    }

    override fun getItemCount(): Int {
        return panels.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView2) as ImageView
        val textView: TextView = view.findViewById(R.id.textView3)
        val favoriteButton: ImageButton = view.findViewById(R.id.isFavoriteButton1)
        val shareButton: ImageButton = view.findViewById(R.id.shareButton1)
        val panelClickView: View = view.findViewById(R.id.panelClickView)
    }

    class PanelDataAdapterListener(
        val favoriteClickListener: (v: View, panel: Panel) -> Unit,
        val shareClickListener: (v: View, panel: Panel) -> Unit,
        val panelClickListener: (v: View, panel: Panel) -> Unit
    )
}
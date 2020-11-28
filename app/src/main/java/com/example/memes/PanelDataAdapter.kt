package com.example.memes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

internal class PanelDataAdapter(private val context: Context, private val panels: List<Panel>) :
    RecyclerView.Adapter<PanelDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cell, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val panel = panels[position]
        Glide.with(context).load(panel.imageLink).into(holder.imageView)
        holder.textView.text = panel.imageDescription
    }

    override fun getItemCount(): Int {
        return panels.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView2) as ImageView
        val textView: TextView = view.findViewById(R.id.textView3)
    }

}
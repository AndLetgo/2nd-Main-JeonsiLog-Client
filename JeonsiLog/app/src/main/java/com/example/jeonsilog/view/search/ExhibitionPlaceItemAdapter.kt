package com.example.jeonsilog.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem


class ExhibitionPlaceItemAdapter(private val items: List<ExhibitionPlaceItem>) : RecyclerView.Adapter<ExhibitionPlaceItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exhibition_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.exhibitionPlaceItemName


        // 이미지 로딩
        //Picasso.get().load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
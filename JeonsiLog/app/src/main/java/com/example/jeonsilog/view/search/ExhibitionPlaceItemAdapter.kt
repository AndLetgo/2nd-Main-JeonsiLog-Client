package com.example.jeonsilog.view.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ExhibitionPlaceItemAdapter(private val context: Context, private val list:List <SearchPlacesInformationEntity>) : RecyclerView.Adapter<ExhibitionPlaceItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_exhibition_place_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exihibition_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(context)
            .load("")
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.iv_exhibition_place_img))

        holder.nameTextView.text = list[position].placeName
        holder.itemView.setOnClickListener {
            //전시장 id

            //(context as MainActivity).loadExtraActivity(0, item.exhibitionId)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
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
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class ExhibitionPlaceItemAdapter(private val context: Context,private val edittext:String, private val list:MutableList <SearchPlacesInformationEntity>) : RecyclerView.Adapter<ExhibitionPlaceItemAdapter.ViewHolder>() {
    var itemPage=0
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_exhibition_place_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exihibition_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        //list.size가 position 보다 커야함
        //list의 마지막요소가 아닌 마지막 전요소에서 비교
        if (list.size>5){
            if (list.size-4==position){
                itemPage+=1
                runBlocking(Dispatchers.IO) {
                    val response = PlaceRepositoryImpl().searchPlaces(encryptedPrefs.getAT(),edittext,itemPage)
                    if(response.isSuccessful && response.body()!!.check){
                        val searchPlaceResponse = response.body()
                        list.addAll(searchPlaceResponse?.informationEntity!!.toMutableList())
                    }
                }
            }
        }
        GlideApp.with(context)
            .load("")
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.iv_exhibition_place_img))

        holder.nameTextView.text = item.placeName
        holder.itemView.setOnClickListener {
            //전시장 id
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
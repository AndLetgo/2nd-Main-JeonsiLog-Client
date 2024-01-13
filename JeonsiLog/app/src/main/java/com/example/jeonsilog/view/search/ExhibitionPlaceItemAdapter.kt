package com.example.jeonsilog.view.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.ExhibitionPlaceRvAdapter
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExhibitionPlaceItemAdapter(private val context: Context, private val list:List <SearchPlacesInformationEntity>) : RecyclerView.Adapter<ExhibitionPlaceItemAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_exhibition_place_name)
        val addressTextView: TextView = view.findViewById(R.id.tv_exhibition_place_address)
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
        val words = list[position].placeAddress!!.split(" ")
        // 앞의 2개의 단어 추출
        if (words.size >= 2) {
            val firstWord = words[0]
            val secondWord = words[1]
            holder.addressTextView.text = "$firstWord $secondWord"
        }
        holder.itemView.setOnClickListener {
            //전시장 id
            listener?.onItemClick(2, list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(type:Int, placeItem: SearchPlacesInformationEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
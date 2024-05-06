package com.example.jeonsilog.view.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.databinding.ItemExihibitionSearchBinding

class AdminSearchPlaceRvAdapter(
    var list: MutableList<SearchPlacesInformationEntity>):
    RecyclerView.Adapter<AdminSearchPlaceRvAdapter.RecycleViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(private val binding: ItemExihibitionSearchBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: SearchPlacesInformationEntity){
            binding.tvExhibitionPlaceName.text = item.placeName

            val words = item.placeAddress.split(" ")
            if(words.size >= 2){
                binding.tvExhibitionPlaceAddress.text = "${words[0]} ${words[1]}"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemExihibitionSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(v: View, data: SearchInformationEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun notifyList(newList: List<SearchPlacesInformationEntity>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
package com.example.jeonsilog.view.admin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.databinding.ItemHomeExhibitionBinding

class AdminSearchExhibitionRvAdapter(
    var list: MutableList<SearchInformationEntity>, private val context: Context):
    RecyclerView.Adapter<AdminSearchExhibitionRvAdapter.RecycleViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(private val binding: ItemHomeExhibitionBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: SearchInformationEntity){
            Log.d("search", "bind: item.exhibitionName: ${item.exhibitionName}")
            binding.tvTitle.text = item.exhibitionName

            if(item.place.placeAddress != null){
                val addressList = item.place.placeAddress.split(" ")
                val address = "${addressList[0]} ${addressList[1]}"
                binding.tvAddress.text = address
            }

            if(item.place.placeName !=null){
                binding.tvPlace.text = item.place.placeName
            }

            var operatingKeyword = ""
            when(item.operatingKeyword){
                "ON_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_on)
                "BEFORE_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_before)
            }
            var priceKeyword = ""
            when(item.priceKeyword){
                "FREE" -> priceKeyword = context.getString(R.string.keyword_free)
                else -> binding.tvKeywordSecond.visibility = View.INVISIBLE
            }

            if(operatingKeyword!=""){
                binding.tvKeywordFirst.text = operatingKeyword
                binding.tvKeywordSecond.text = priceKeyword
            }else{
                if(priceKeyword!=""){
                    binding.tvKeywordSecond.visibility = View.INVISIBLE
                    binding.tvKeywordFirst.text = priceKeyword
                }else {
                    binding.tvKeywordFirst.visibility = View.INVISIBLE
                }
            }

            Glide.with(context)
                .load(item.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivPoster)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemHomeExhibitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(list[position])

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, list[position], position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(v: View, data: SearchInformationEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun notifyList(newList: List<SearchInformationEntity>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
package com.example.jeonsilog.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.ItemHomeExhibitionVer2Binding

class HomeEndSoonRvAdapter(private val homeRvList:List<ExhibitionsInfo>, private val context:Context):
    RecyclerView.Adapter<HomeEndSoonRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(val binding: ItemHomeExhibitionVer2Binding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = homeRvList[position]
            binding.tvExhibitionName.text = item.exhibitionName

            if(item.place.placeName !=null){
                binding.tvExhibitionPlace.visibility = View.VISIBLE
                binding.tvExhibitionPlace.text = item.place.placeName
            }else
                binding.tvExhibitionPlace.visibility = View.GONE

            var operatingKeyword = ""
            when(item.operatingKeyword){
                "ON_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_on)
                "BEFORE_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_before)
            }
            var priceKeyword = ""
            when(item.priceKeyword){
                "FREE" -> priceKeyword = context.getString(R.string.keyword_free)
                else -> binding.tvKeywordSecond.visibility = View.GONE
            }

            if(!operatingKeyword.isNullOrEmpty()){
                binding.tvKeywordFirst.visibility = View.VISIBLE
                binding.tvKeywordFirst.text = operatingKeyword
                if(!priceKeyword.isNullOrEmpty()){
                    binding.tvKeywordSecond.visibility = View.VISIBLE
                    binding.tvKeywordSecond.text = priceKeyword
                }
            }else{
                if(!priceKeyword.isNullOrEmpty()){
                    binding.tvKeywordSecond.visibility = View.GONE
                    binding.tvKeywordFirst.visibility = View.VISIBLE
                    binding.tvKeywordFirst.text = priceKeyword
                }else {
                    binding.tvKeywordFirst.visibility = View.GONE
                }
            }

            Glide.with(context)
                .load(item.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivExhibitionImg)

        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemHomeExhibitionVer2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = homeRvList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener{
                listener?.onItemClick(holder.itemView, homeRvList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ExhibitionsInfo, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

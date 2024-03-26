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

class HomeNewStartRvAdapter(private val homeRvList:List<ExhibitionsInfo>, private val context:Context):
    RecyclerView.Adapter<HomeNewStartRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(val binding: ItemHomeExhibitionVer2Binding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = homeRvList[position]
            binding.tvExhibitionName.text = item.exhibitionName

            if(item.place.placeName !=null){
                binding.tvExhibitionPlace.text = item.place.placeName
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
                binding.tvKeywordFirst.visibility = View.VISIBLE
                binding.tvKeywordSecond.visibility = View.VISIBLE
                binding.tvKeywordFirst.text = operatingKeyword
                binding.tvKeywordSecond.text = priceKeyword
            }else{
                if(priceKeyword!=""){
                    binding.tvKeywordSecond.visibility = View.INVISIBLE
                    binding.tvKeywordFirst.visibility = View.VISIBLE
                    binding.tvKeywordFirst.text = priceKeyword
                }else {
                    binding.tvKeywordFirst.visibility = View.INVISIBLE
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

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ExhibitionsInfo, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.place.GetPlacesInformationEntity
import com.example.jeonsilog.databinding.ItemExhibitionPlaceBinding
import com.example.jeonsilog.widget.utils.DateUtil

class ExhibitionPlaceRvAdapter(private val placeList: List<GetPlacesInformationEntity>, private val context: Context) :
    RecyclerView.Adapter<ExhibitionPlaceRvAdapter.RecycleViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(private val binding: ItemExhibitionPlaceBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: GetPlacesInformationEntity){
            binding.tvExhibitionName.text = item.exhibitionName
            val date = DateUtil().editStringDate(item.startDate) + " ~ " + DateUtil().editStringDate(item.endDate)
            binding.tvExhibitionDate.text = date

            var operatingKeyword = ""
            when(item.operatingKeyword){
                "ON_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_on)
                "BEFORE_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_before)
            }
            var priceKeyword = ""
            when(item.priceKeyword){
                "FREE" -> priceKeyword = context.getString(R.string.keyword_free)
                else -> binding.tvKeywordSecond.isGone = true
            }

            if(operatingKeyword!=""){
                binding.tvKeywordFirst.text = operatingKeyword
                binding.tvKeywordSecond.text = priceKeyword
            }else{
                if(priceKeyword!=""){
                    binding.tvKeywordSecond.isGone = true
                    binding.tvKeywordFirst.text = priceKeyword
                }else {
                    binding.tvKeywordFirst.isGone = true
                }
            }

            Glide.with(context)
                .load(item.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivPoster)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExhibitionPlaceRvAdapter.RecycleViewHolder {
        val binding = ItemExhibitionPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ExhibitionPlaceRvAdapter.RecycleViewHolder,
        position: Int
    ) {
        holder.bind(placeList[position])

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, placeList[position], position)
            }
        }
    }

    override fun getItemCount(): Int = placeList.size

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GetPlacesInformationEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
package com.example.jeonsilog.view.exhibition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.databinding.ItemExhibitionPlaceBinding
import com.example.jeonsilog.databinding.ItemReviewReplyBinding
import com.example.jeonsilog.viewmodel.ExhibitionModel

class ExhibitionPlaceRvAdapter(private val exhibitionList: List<ExhibitionModel>) :
    RecyclerView.Adapter<ExhibitionPlaceRvAdapter.RecycleViewHolder>() {

    inner class RecycleViewHolder(private val binding: ItemExhibitionPlaceBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: ExhibitionModel){
//            binding.ivExhibitionPoster.setImageDrawable()
            binding.tvExhibitionName.text = item.title
            binding.tvExhibitionDate.text = item.date
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
        holder.bind(exhibitionList[position])
    }

    override fun getItemCount(): Int = exhibitionList.size
}
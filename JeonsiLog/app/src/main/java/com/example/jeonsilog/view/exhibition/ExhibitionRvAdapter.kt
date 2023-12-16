package com.example.jeonsilog.view.exhibition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeonsilog.databinding.ItemExhibitionReviewBinding

class ExhibitionRvAdapter(private val ReviewList:List<ReviewModel>): RecyclerView.Adapter<ExhibitionRvAdapter.RecycleViewHolder>()  {

    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvUserName.text = ReviewList[position].userId.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemExhibitionReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = ReviewList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)
    }

}
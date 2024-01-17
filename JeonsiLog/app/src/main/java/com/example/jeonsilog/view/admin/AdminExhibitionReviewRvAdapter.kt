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
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.ItemExhibitionReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.widget.utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminExhibitionReviewRvAdapter(private val reviewList:MutableList<GetReviewsExhibitionInformationEntity>, private val context:Context):
    RecyclerView.Adapter<AdminExhibitionReviewRvAdapter.RecycleViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = reviewList[position]
            binding.ibMenu.visibility = View.INVISIBLE

            binding.tvUserName.text = item.nickname
            binding.tvReviewContent.text = item.contents
            if(item.rate==0.0){
                binding.brbExhibitionReviewRating.visibility = View.GONE
            }else{
                binding.brbExhibitionReviewRating.rating = item.rate.toFloat()
            }
            binding.tvReplyCount.text = "${context.getString(R.string.exhibition_reply)} ${item.numReply}"
            Log.d("TAG", "bind: reply count: ${item.numReply}")
            binding.tvReviewDate.text = DateUtil().formatElapsedTime(item.createdDate)
            if(binding.ivProfile!=null){
                Glide.with(context)
                    .load(item.imgUrl)
                    .transform(CenterCrop(), RoundedCorners(80))
                    .into(binding.ivProfile)
            }else{
                Glide.with(context)
                    .load(R.drawable.illus_empty_poster)
                    .transform(CenterCrop(), RoundedCorners(80))
                    .into(binding.ivProfile)
            }

            binding.tvDelete.setOnClickListener{
                listener?.deleteItem(position, item.reviewId)
            }
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

    override fun onBindViewHolder(
        holder: AdminExhibitionReviewRvAdapter.RecycleViewHolder, position: Int) {
        holder.bind(position)

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, reviewList[position], position)
            }
        }
    }

    override fun getItemCount(): Int = reviewList.size

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GetReviewsExhibitionInformationEntity, position: Int)
        fun deleteItem(position: Int, reviewId:Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun deleteItem(position: Int){
        reviewList.removeAt(position)
        notifyItemRemoved(position)
    }
}
package com.example.jeonsilog.view.exhibition

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

class ExhibitionReviewRvAdapter(
    private val reviewList:List<GetReviewsExhibitionInformationEntity>,
    private val context: Context):
    RecyclerView.Adapter<ExhibitionReviewRvAdapter.RecycleViewHolder>()  {

    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: GetReviewsExhibitionInformationEntity){
            Log.d("review", "bind: adapter nickname: ${item.nickname}")
            binding.tvUserName.text = item.nickname
            binding.tvReviewContent.text = item.contents
            binding.brbExhibitionReviewRating.rating = item.rate.toFloat()
            binding.tvReplyCount.text = "${context.getString(R.string.exhibition_reply)} ${item.numReply}"
//            binding.tvReviewDate.text = item.
            Glide.with(context)
                .load(item.imgUrl)
                .transform(CenterCrop(), RoundedCorners(80))
                .into(binding.ivProfile)

            binding.ibMenu.setOnClickListener{
                listener?.onMenuBtnClick(it)
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

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(reviewList[position])

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, reviewList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GetReviewsExhibitionInformationEntity, position: Int)
        fun onMenuBtnClick(btn:View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
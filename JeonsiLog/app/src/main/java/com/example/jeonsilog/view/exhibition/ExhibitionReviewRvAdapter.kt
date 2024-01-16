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
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs

class ExhibitionReviewRvAdapter(
    private val reviewList:MutableList<GetReviewsExhibitionInformationEntity>,
    private val context: Context):
    RecyclerView.Adapter<ExhibitionReviewRvAdapter.RecycleViewHolder>()  {

    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = reviewList[position]
            if(item.userId == encryptedPrefs.getUI()){
                listener?.saveUserReview(item, position)
            }

            binding.tvUserName.text = item.nickname
            binding.tvReviewContent.text = item.contents
            Log.d("TAG", "bind: item.rate: ${item.rate}")
            if(item.rate==null || item.rate==0.0){
                binding.brbExhibitionReviewRating.visibility = View.GONE
            }else{
                binding.brbExhibitionReviewRating.visibility = View.VISIBLE
                binding.brbExhibitionReviewRating.rating = item.rate.toFloat()
            }
            binding.tvReplyCount.text = "${context.getString(R.string.exhibition_reply)} ${item.numReply}"
            binding.tvReviewDate.text = DateUtil().formatElapsedTime(item.createdDate)
            Glide.with(context)
                .load(item.imgUrl)
                .transform(CenterCrop(), RoundedCorners(80))
                .into(binding.ivProfile)

//            binding.ibMenu.setOnClickListener{
//                if(item.userId == encryptedPrefs.getUI()){
//                    listener?.onMenuBtnClick(it, 0, item.reviewId, position)
//                }else{
//                    listener?.onMenuBtnClick(it, 1, item.reviewId, position)
//                }
//            }
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
        holder.bind(position)

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, reviewList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GetReviewsExhibitionInformationEntity, position: Int)
        fun onMenuBtnClick(btn:View, user:Int, contentId: Int, position: Int)
        fun saveUserReview(data: GetReviewsExhibitionInformationEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun removeItem(position: Int){
        reviewList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceItem(review: GetReviewsExhibitionInformationEntity, position: Int){
        if(reviewList.size>0){
            reviewList[position] = review
            notifyItemChanged(position)
        }
    }
}
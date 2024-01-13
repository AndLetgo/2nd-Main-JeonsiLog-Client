package com.example.jeonsilog.view.otheruser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsEntity
import com.example.jeonsilog.databinding.ItemOtherUserRatingBinding
import com.example.jeonsilog.databinding.ItemOtherUserReviewBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlin.IllegalArgumentException

class OtherUserRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeRatingViewHolder(private val binding: ItemOtherUserRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyRatingsEntity){
            binding.tvOtherUserRatingItemTitle.text = data.exhibitionName
            binding.rbOtherUserRatingItemRating.rating = data.rate.toFloat()

            itemView.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).loadExtraActivity(type = 0, newTargetId = data.exhibitionId)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).loadExtraActivity(type = 0, newTargetId = data.exhibitionId)
                }

            }
        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemOtherUserReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetReviewsEntity) {
            GlideApp.with(binding.ivOtherUserReviewExhibitionImg)
                .load(data.exhibitionImgUrl)
                .centerCrop()
                .into(binding.ivOtherUserReviewExhibitionImg)

            binding.tvOtherUserReviewContent.text = SpannableStringUtil().boldTextBetweenBrackets(data.contents)

            itemView.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).loadExtraActivity(type = 0, newTargetId = data.exhibitionId)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).loadExtraActivity(type = 0, newTargetId = data.exhibitionId)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return TypeRatingViewHolder(
                    ItemOtherUserRatingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            1 -> {
                return TypeReviewViewHolder(
                    ItemOtherUserReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("지원하지 않은 ViewType: $viewType")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.type) {
            0 -> {
                val ratingData = list[position] as GetMyRatingsEntity
                holder as OtherUserRvAdapter<*>.TypeRatingViewHolder
                holder.bind(ratingData)
            }
            1 -> {
                val reviewData = list[position] as GetReviewsEntity
                holder as OtherUserRvAdapter<*>.TypeReviewViewHolder
                holder.bind(reviewData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}
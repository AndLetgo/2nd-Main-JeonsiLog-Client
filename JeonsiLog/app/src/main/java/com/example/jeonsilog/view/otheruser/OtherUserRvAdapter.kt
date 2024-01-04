package com.example.jeonsilog.view.otheruser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsDataEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsDataEntity
import com.example.jeonsilog.databinding.ItemOtherUserRatingBinding
import com.example.jeonsilog.databinding.ItemOtherUserReviewBinding
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlin.IllegalArgumentException

class OtherUserRvAdapter<T>(private val list: MutableList<T>, private val type: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeRatingViewHolder(private val binding: ItemOtherUserRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyRatingsDataEntity){
            binding.tvOtherUserRatingItemTitle.text = data.exhibitionName
            binding.rbOtherUserRatingItemRating.rating = data.rate.toFloat()

            // 클릭리스너 - 해당 전시회 상세 페이지로 이동
        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemOtherUserReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetReviewsDataEntity) {
            GlideApp.with(binding.ivOtherUserReviewExhibitionImg)
                .load(data.exhibitionImgUrl)
                .centerCrop()
                .into(binding.ivOtherUserReviewExhibitionImg)

            binding.tvOtherUserReviewContent.text = SpannableStringUtil().boldTextBetweenBrackets(data.contents)

            // 클릭리스너 - 해당 전시회 상세 페이지
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
                val ratingData = list[position] as GetMyRatingsDataEntity
                holder as OtherUserRvAdapter<*>.TypeRatingViewHolder
                holder.bind(ratingData)
            }
            1 -> {
                val reviewData = list[position] as GetReviewsDataEntity
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
package com.example.jeonsilog.view.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.databinding.ItemMyPageRatingBinding
import com.example.jeonsilog.databinding.ItemMyPageReviewBinding
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.SpannableStringUtil

class MyPageRvAdapter<T>(private val list: List<T>, private val type: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class TypeRatingViewHolder(private val binding: ItemMyPageRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MyPageRatingModel){
            binding.tvMypageRatingItemTitle.text = data.title
            binding.rbMypageRatingItemRating.rating = data.rating

            // 클릭리스너 - 해당 전시회 상세 페이지로 이동
        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemMyPageReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MyPageReviewModel) {
            GlideApp.with(binding.ivMypageReviewExhibitionImg)
                .load(data.imgUrl)
                .into(binding.ivMypageReviewExhibitionImg)

            binding.tvMypageReviewContent.text = SpannableStringUtil().boldTextBetweenBrackets(data.content)

            // 클릭리스너 - 해당 전시회 상세 페이지
        }
    }

//    inner class TypeInterestViewHolder(private val binding: ItemMyPageInterestBinding): RecyclerView.ViewHolder(binding.root){
//        fun bind(data: MyPageInterestModel) {
//        }
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return TypeRatingViewHolder(
                    ItemMyPageRatingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            2 -> {
                return TypeReviewViewHolder(
                    ItemMyPageReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
//                return TypeInterestViewHolder(
//                    ItemMyPageInterestBinding.inflate(
//                        LayoutInflater.from(parent.context),
//                        parent,
//                        false
//                    )
//                )
                return TypeReviewViewHolder(
                    ItemMyPageReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.type) {
            0 -> {
                val ratingData = list[position] as MyPageRatingModel
                holder as MyPageRvAdapter<*>.TypeRatingViewHolder
                holder.bind(ratingData)
            }
            1 -> {
                val reviewData = list[position] as MyPageReviewModel
                holder as MyPageRvAdapter<*>.TypeReviewViewHolder
                holder.bind(reviewData)
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

}
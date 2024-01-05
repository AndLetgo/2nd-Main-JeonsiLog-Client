package com.example.jeonsilog.view.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.data.remote.dto.interest.GetInterestInformationEntity
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsDataEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsDataEntity
import com.example.jeonsilog.databinding.ItemMyPageInterestBinding
import com.example.jeonsilog.databinding.ItemMyPageRatingBinding
import com.example.jeonsilog.databinding.ItemMyPageReviewBinding
import com.example.jeonsilog.repository.interest.InterestRepositoryImpl
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MyPageRvAdapter<T>(private val list: MutableList<T>, private val type: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeRatingViewHolder(private val binding: ItemMyPageRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyRatingsDataEntity){
            binding.tvMypageRatingItemTitle.text = data.exhibitionName
            binding.rbMypageRatingItemRating.rating = data.rate.toFloat()

            // 클릭리스너 - 해당 전시회 상세 페이지로 이동
            itemView.setOnClickListener {

            }

        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemMyPageReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetReviewsDataEntity) {
            GlideApp.with(binding.ivMypageReviewExhibitionImg)
                .load(data.exhibitionImgUrl)
                .centerCrop()
                .into(binding.ivMypageReviewExhibitionImg)

            binding.tvMypageReviewTitle.text = data.exhibitionName
            binding.tvMypageReviewContent.text = data.contents

            // 클릭리스너 - 해당 전시회 상세 페이지로 이동로 이동
            itemView.setOnClickListener {

            }
        }
    }

    inner class TypeInterestViewHolder(private val binding: ItemMyPageInterestBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetInterestInformationEntity) {
            GlideApp.with(binding.ivMypageInterestExhibitionImg)
                .load(data.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivMypageInterestExhibitionImg)

            binding.tvMypageInterestTitle.text = data.exhibitionName
            binding.tvMypageInterestAddress.text = data.placeName

            when(data.operatingKeyword){
                "ON_DISPLAY" -> binding.tvMypageInterestKeywordOn.visibility = View.VISIBLE
                "BEFORE_DISPLAY" -> binding.tvMypageInterestKeywordBefore.visibility = View.VISIBLE
                else -> {
                    binding.tvMypageInterestKeywordBefore.visibility = View.GONE
                    binding.tvMypageInterestKeywordOn.visibility = View.GONE
                }
            }

            when(data.priceKeyword){
                "FREE" -> binding.tvMypageInterestKeywordFree.visibility = View.VISIBLE
                else -> binding.tvMypageInterestKeywordFree.visibility = View.GONE
            }

            binding.ibMypageInterest.setOnClickListener {
                list.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

                // 서버에 즐겨찾기 해제 요청
                CoroutineScope(Dispatchers.IO).launch{
                    InterestRepositoryImpl().deleteInterest(encryptedPrefs.getAT(), data.exhibitionId)
                }
            }

            // 클릭리스너 - 해당 전시회 상세 페이지
            itemView.setOnClickListener {

            }
        }
    }


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
            1 -> {
                return TypeReviewViewHolder(
                    ItemMyPageReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return TypeInterestViewHolder(
                    ItemMyPageInterestBinding.inflate(
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
                val ratingData = list[position] as GetMyRatingsDataEntity
                holder as MyPageRvAdapter<*>.TypeRatingViewHolder
                holder.bind(ratingData)
            }
            1 -> {
                val reviewData = list[position] as GetReviewsDataEntity
                holder as MyPageRvAdapter<*>.TypeReviewViewHolder
                holder.bind(reviewData)
            }
            2 -> {
                val interestData = list[position] as GetInterestInformationEntity
                holder as MyPageRvAdapter<*>.TypeInterestViewHolder
                holder.bind(interestData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}
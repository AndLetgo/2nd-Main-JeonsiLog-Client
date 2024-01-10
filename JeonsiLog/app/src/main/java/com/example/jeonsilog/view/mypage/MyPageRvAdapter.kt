package com.example.jeonsilog.view.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.interest.GetInterestEntity
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsEntity
import com.example.jeonsilog.databinding.ItemMyPageInterestBinding
import com.example.jeonsilog.databinding.ItemMyPageRatingBinding
import com.example.jeonsilog.databinding.ItemMyPageReviewBinding
import com.example.jeonsilog.repository.interest.InterestRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MyPageRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeRatingViewHolder(private val binding: ItemMyPageRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyRatingsEntity){
            binding.tvMypageRatingItemTitle.text = data.exhibitionName
            binding.rbMypageRatingItemRating.rating = data.rate.toFloat()

            itemView.setOnClickListener {
                (context as MainActivity).loadExtraActivity(type = 0, newExhibitionId = data.exhibitionId)

            }
        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemMyPageReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetReviewsEntity) {
            GlideApp.with(binding.ivMypageReviewExhibitionImg)
                .load(data.exhibitionImgUrl)
                .centerCrop()
                .into(binding.ivMypageReviewExhibitionImg)

            binding.tvMypageReviewTitle.text = data.exhibitionName
            binding.tvMypageReviewContent.text = data.contents

            itemView.setOnClickListener {
                (context as MainActivity).loadExtraActivity(type = 0, newExhibitionId = data.exhibitionId)
            }
        }
    }

    inner class TypeInterestViewHolder(private val binding: ItemMyPageInterestBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetInterestEntity) {
            GlideApp.with(binding.ivMypageInterestExhibitionImg)
                .load(data.imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivMypageInterestExhibitionImg)

            binding.tvMypageInterestTitle.text = data.exhibitionName

            binding.tvMypageInterestAddress.text = placeName(data.placeName)

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

            var state = true
            binding.ibMypageInterest.background = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_interest_active)

            binding.ibMypageInterest.setOnClickListener {
                if(state){
                    binding.ibMypageInterest.background = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_interest_inactive)
                    CoroutineScope(Dispatchers.IO).launch{
                        InterestRepositoryImpl().deleteInterest(encryptedPrefs.getAT(), data.exhibitionId)
                    }
                } else {
                    binding.ibMypageInterest.background = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_interest_active)
                    CoroutineScope(Dispatchers.IO).launch{
                        InterestRepositoryImpl().postInterest(encryptedPrefs.getAT(), data.exhibitionId)
                    }
                }

                state = !state
            }

            itemView.setOnClickListener {
                (context as MainActivity).loadExtraActivity(type = 0, newExhibitionId = data.exhibitionId)
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
                val ratingData = list[position] as GetMyRatingsEntity
                holder as MyPageRvAdapter<*>.TypeRatingViewHolder
                holder.bind(ratingData)
            }
            1 -> {
                val reviewData = list[position] as GetReviewsEntity
                holder as MyPageRvAdapter<*>.TypeReviewViewHolder
                holder.bind(reviewData)
            }
            2 -> {
                val interestData = list[position] as GetInterestEntity
                holder as MyPageRvAdapter<*>.TypeInterestViewHolder
                holder.bind(interestData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    private fun placeName(input: String?): String {
        return if(input.isNullOrEmpty()){
            ""
        } else {
            val index = input.indexOf('(')
            if (index != -1) {
                input.substring(0, index)
            } else {
                input
            }
        }
    }
}
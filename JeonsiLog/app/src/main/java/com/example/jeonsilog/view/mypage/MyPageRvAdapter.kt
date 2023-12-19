package com.example.jeonsilog.view.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.databinding.ItemMyPageInterestBinding
import com.example.jeonsilog.databinding.ItemMyPageRatingBinding
import com.example.jeonsilog.databinding.ItemMyPageReviewBinding

class MyPageRvAdapter<T>(private val list: List<T>, private val type: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class TypeRatingViewHolder(private val binding: ItemMyPageRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MyPageRatingModel){
            binding.tvMypageRatingItemTitle.text = data.title
            binding.rbMypageRatingItemRating.rating = data.rating

            // 클릭리스너 - 해당 전시회 상세 페이지로 이동
        }
    }

    inner class TypeReviewViewHolder(private val binding: ItemMyPageReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myPageReviewModel: MyPageReviewModel) {

        }
    }

    inner class TypeInterestViewHolder(private val binding: ItemMyPageInterestBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myPageInterestModel: MyPageInterestModel) {

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
        when (holder) {
            is MyPageRvAdapter<*>.TypeRatingViewHolder -> {
                list as List<MyPageRatingModel>
                holder.bind(list[position])
            }
            is MyPageRvAdapter<*>.TypeReviewViewHolder -> {
                list as List<MyPageReviewModel>
                holder.bind(list[position])
            }
            is MyPageRvAdapter<*>.TypeInterestViewHolder -> {
                list as List<MyPageInterestModel>
                holder.bind(list[position])
            }
            // 다른 뷰 홀더 유형이 필요한 경우 처리
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

}
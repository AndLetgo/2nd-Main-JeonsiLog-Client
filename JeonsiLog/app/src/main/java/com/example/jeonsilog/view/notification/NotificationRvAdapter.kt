package com.example.jeonsilog.view.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.databinding.ItemNotiFollowingBinding
import com.example.jeonsilog.databinding.ItemNotiMyactivityBinding
import com.example.jeonsilog.widget.utils.GlideApp

class NotificationRvAdapter(private val notiList: List<NotificationModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class TypeFollowingViewHolder(private val binding: ItemNotiFollowingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationModel){
            GlideApp.with(binding.ivNotiProfile)
                .load(data.profileImg)
                .circleCrop()
                .into(binding.ivNotiProfile)

            binding.tvNotiTitle.text = data.title
            binding.tvNotiContent.text = data.content
            binding.tvNotiTime.text = data.time
            // 클릭리스너 - 해당 전시회로 이동
        }
    }

    inner class TypeExhibitionViewHolder(private val binding: ItemNotiMyactivityBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: NotificationModel){
            GlideApp.with(binding.ivNotiExhibition)
                .load(data.profileImg)
                .circleCrop()
                .into(binding.ivNotiExhibition)

            binding.tvNotiTitle.text = data.title
            binding.tvNotiContent.text = data.content
            binding.tvNotiTime.text = data.time

            // 클릭리스너 - 해당 전시회로 이동
        }
    }


    inner class TypeFollowViewHolder(private val binding: ItemNotiFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: NotificationModel){
            GlideApp.with(binding.ivNotiProfile)
                .load(data.profileImg)
                .circleCrop()
                .into(binding.ivNotiProfile)

            binding.tvNotiTitle.text = data.title
            binding.tvNotiContent.text = data.content
            binding.tvNotiTime.text = data.time

            // 클릭리스너 - 해당 유저 프로필로 이동
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeFollowViewHolder -> {
                holder.bind(notiList[position])
            }
            is TypeExhibitionViewHolder -> {
                holder.bind(notiList[position])
            }
            is TypeFollowingViewHolder -> {
                holder.bind(notiList[position])
            }
            // 다른 뷰 홀더 유형이 필요한 경우 처리
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }

    }

    override fun getItemCount(): Int = notiList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return TypeFollowingViewHolder(
                    ItemNotiFollowingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            2 -> {
                return TypeExhibitionViewHolder(
                    ItemNotiMyactivityBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return TypeFollowViewHolder(
                    ItemNotiFollowingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return notiList[position].type
    }
}
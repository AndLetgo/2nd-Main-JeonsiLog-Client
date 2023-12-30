package com.example.jeonsilog.view.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemMyPageListFollowBinding
import com.example.jeonsilog.databinding.ItemMyPageListFollowingBinding
import com.example.jeonsilog.widget.utils.GlideApp
import java.lang.IllegalArgumentException

class MyPageListRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeFollowViewHolder(private val binding: ItemMyPageListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MyPageListFollowerModel){
            GlideApp.with(binding.ivMypageListFollowProfile)
                .load(data.url)
                .optionalCircleCrop()
                .into(binding.ivMypageListFollowProfile)

            binding.tvMypageListFollowNick.text = data.nick
            if(data.isFollowBack){
                binding.btnMypageListFollowing.text = getString(context, R.string.btn_following)
                binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn)
                binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.gray_medium))
            } else {
                binding.btnMypageListFollowing.text = getString(context, R.string.btn_f4f)
                binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.basic_white))
            }

            binding.ibMypageListFollowerDelete.setOnClickListener {
                list.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

            binding.btnMypageListFollowing.setOnClickListener {
                if(data.isFollowBack){
                    binding.btnMypageListFollowing.text = getString(context, R.string.btn_f4f)
                    binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                    binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.basic_white))
                    list[adapterPosition] = MyPageListFollowerModel(data.id, data.url, data.nick, false) as T
                    notifyItemChanged(adapterPosition)

                } else {
                    binding.btnMypageListFollowing.text = getString(context, R.string.btn_following)
                    binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn)
                    binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.gray_medium))
                    list[adapterPosition] = MyPageListFollowerModel(data.id, data.url, data.nick, true) as T
                    notifyItemChanged(adapterPosition)
                }
            }

            binding.ivMypageListFollowProfile.setOnClickListener{
                // 타 유저 프로필로 이동
            }

            binding.tvMypageListFollowNick.setOnClickListener {
                // 타 유저 프로필로 이동
            }
        }
    }


    inner class TypeFollowingViewHolder(private val binding: ItemMyPageListFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MyPageListFollowingModel) {
            GlideApp.with(binding.ivMypageListFollowingProfile)
                .load(data.url)
                .optionalCircleCrop()
                .into(binding.ivMypageListFollowingProfile)

            binding.tvMypageListFollowNick.text = data.nick

            binding.btnMypageListFollowing.setOnClickListener {
                list.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

                // 서버에 팔로우 취소 요청
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                return TypeFollowViewHolder(
                    ItemMyPageListFollowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return TypeFollowingViewHolder(
                    ItemMyPageListFollowingBinding.inflate(
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
                val followerData = list[position] as MyPageListFollowerModel
                holder as MyPageListRvAdapter<*>.TypeFollowViewHolder
                holder.bind(followerData)
            }
            1 -> {
                val followingData = list[position] as MyPageListFollowingModel
                holder as MyPageListRvAdapter<*>.TypeFollowingViewHolder
                holder.bind(followingData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}

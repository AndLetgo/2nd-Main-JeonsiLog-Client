package com.example.jeonsilog.view.otheruser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerInformation
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingInformation
import com.example.jeonsilog.databinding.ItemOtherUserListFollowBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class OtherUserListRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeFollowViewHolder(private val binding: ItemOtherUserListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetOtherFollowerInformation){
            GlideApp.with(binding.ivOtherUserListFollowProfile)
                .load(data.profileImgUrl)
                .optionalCircleCrop()
                .into(binding.ivOtherUserListFollowProfile)

            binding.tvOtherUserListFollowNick.text = data.nickname
            if(data.ifollow){
                binding.btnOtherUserListFollowing.text = getString(context, R.string.btn_following)
                binding.btnOtherUserListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn)
                binding.btnOtherUserListFollowing.setTextColor(getColor(context, R.color.gray_medium))
            } else if (data.followMe){
                binding.btnOtherUserListFollowing.text = getString(context, R.string.btn_f4f)
                binding.btnOtherUserListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                binding.btnOtherUserListFollowing.setTextColor(getColor(context, R.color.basic_white))
            } else {
                binding.btnOtherUserListFollowing.text = getString(context, R.string.btn_follow)
                binding.btnOtherUserListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                binding.btnOtherUserListFollowing.setTextColor(getColor(context, R.color.basic_white))
            }

            if(data.followUserId == encryptedPrefs.getUI()){
                binding.btnOtherUserListFollowing.visibility = View.GONE
            } else {
                binding.btnOtherUserListFollowing.visibility = View.VISIBLE
            }

            binding.btnOtherUserListFollowing.setOnClickListener {
                if(data.ifollow){
                    list[adapterPosition] = GetOtherFollowerInformation(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = false,
                        followMe = data.followMe) as T

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)

                } else {
                    list[adapterPosition] = GetOtherFollowerInformation(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = true,
                        followMe = data.followMe) as T
                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)
                }
                isFollowerUpdate.value = true
            }

            binding.ivOtherUserListFollowProfile.setOnClickListener{
                (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
            }

            binding.tvOtherUserListFollowNick.setOnClickListener {
                (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
            }
        }
    }


    inner class TypeFollowingViewHolder(private val binding: ItemOtherUserListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetOtherFollowingInformation) {
            GlideApp.with(binding.ivOtherUserListFollowProfile)
                .load(data.profileImgUrl)
                .optionalCircleCrop()
                .into(binding.ivOtherUserListFollowProfile)

            binding.tvOtherUserListFollowNick.text = data.nickname
            if(data.ifollow){
                binding.btnOtherUserListFollowing.text = getString(context, R.string.btn_following)
                binding.btnOtherUserListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn)
                binding.btnOtherUserListFollowing.setTextColor(getColor(context, R.color.gray_medium))
            } else {
                binding.btnOtherUserListFollowing.text = getString(context, R.string.btn_follow)
                binding.btnOtherUserListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                binding.btnOtherUserListFollowing.setTextColor(getColor(context, R.color.basic_white))
            }

            if(data.followUserId == encryptedPrefs.getUI()){
                binding.btnOtherUserListFollowing.visibility = View.GONE
            } else {
                binding.btnOtherUserListFollowing.visibility = View.VISIBLE
            }

            binding.btnOtherUserListFollowing.setOnClickListener {
                if(data.ifollow){
                    list[adapterPosition] = GetOtherFollowingInformation(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = false) as T

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)

                } else {
                    list[adapterPosition] = GetOtherFollowingInformation(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = true) as T
                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)
                }
                isFollowingUpdate.value = true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TypeFollowViewHolder(
            ItemOtherUserListFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.type) {
            0 -> {
                val followerData = list[position] as GetOtherFollowerInformation
                holder as OtherUserListRvAdapter<*>.TypeFollowViewHolder
                holder.bind(followerData)
            }
            1 -> {
                val followingData = list[position] as GetOtherFollowingInformation
                holder as OtherUserListRvAdapter<*>.TypeFollowingViewHolder
                holder.bind(followingData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}

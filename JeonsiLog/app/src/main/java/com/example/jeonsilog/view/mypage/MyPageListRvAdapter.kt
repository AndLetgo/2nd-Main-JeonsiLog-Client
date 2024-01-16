package com.example.jeonsilog.view.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowerEntity
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowingEntity
import com.example.jeonsilog.databinding.ItemMyPageListFollowBinding
import com.example.jeonsilog.databinding.ItemMyPageListFollowingBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class MyPageListRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context, private val listener: FollowingListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeFollowViewHolder(private val binding: ItemMyPageListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyFollowerEntity){
            GlideApp.with(binding.ivMypageListFollowProfile)
                .load(data.profileImgUrl)
                .optionalCircleCrop()
                .into(binding.ivMypageListFollowProfile)

            binding.tvMypageListFollowNick.text = data.nickname
            if(data.ifollow){
                binding.btnMypageListFollowing.text = getString(context, R.string.btn_following)
                binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn)
                binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.gray_medium))
            } else {
                binding.btnMypageListFollowing.text = getString(context, R.string.btn_f4f)
                binding.btnMypageListFollowing.background = getDrawable(context, R.drawable.shape_corner_round_follower_btn_activate)
                binding.btnMypageListFollowing.setTextColor(getColor(context, R.color.basic_white))
            }

            binding.ibMypageListFollowerDelete.setOnClickListener {
                runBlocking(Dispatchers.IO){
                    FollowRepositoryImpl().deleteFollower(encryptedPrefs.getAT(), data.followUserId)
                }
                runBlocking {
                    list.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                isFollowerUpdate.value = true
            }

            binding.btnMypageListFollowing.setOnClickListener {
                if(data.ifollow){
                    list[adapterPosition] = GetMyFollowerEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = false) as T

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)

                } else {
                    list[adapterPosition] = GetMyFollowerEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = true) as T

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)
                }
                isFollowerUpdate.value = true
            }

            binding.ivMypageListFollowProfile.setOnClickListener{
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }

            binding.tvMypageListFollowNick.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }
        }
    }


    inner class TypeFollowingViewHolder(private val binding: ItemMyPageListFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetMyFollowingEntity) {
            GlideApp.with(binding.ivMypageListFollowingProfile)
                .load(data.profileImgUrl)
                .optionalCircleCrop()
                .into(binding.ivMypageListFollowingProfile)

            binding.tvMypageListFollowNick.text = data.nickname

            binding.btnMypageListFollowing.setOnClickListener {
                list.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

                runBlocking(Dispatchers.IO) {
                    FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                }
                isFollowingUpdate.value = true

                if(list.isEmpty()){
                    listener?.setEmpty()
                }
            }

            binding.ivMypageListFollowingProfile.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }

            binding.tvMypageListFollowNick.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
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
                val followerData = list[position] as GetMyFollowerEntity
                holder as MyPageListRvAdapter<*>.TypeFollowViewHolder
                holder.bind(followerData)
            }
            1 -> {
                val followingData = list[position] as GetMyFollowingEntity
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

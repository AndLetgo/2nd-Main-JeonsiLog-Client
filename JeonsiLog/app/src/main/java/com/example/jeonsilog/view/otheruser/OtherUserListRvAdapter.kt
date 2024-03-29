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
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerEntity
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingEntity
import com.example.jeonsilog.databinding.ItemOtherUserListFollowBinding
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

class OtherUserListRvAdapter<T>(private val list: MutableList<T>, private val type: Int, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeFollowViewHolder(private val binding: ItemOtherUserListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetOtherFollowerEntity){
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
                    list[adapterPosition] = GetOtherFollowerEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = false,
                        followMe = data.followMe) as T

                   showDialog()

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                    }

                    notifyItemChanged(adapterPosition)

                    dismissDialog()

                } else {
                    list[adapterPosition] = GetOtherFollowerEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = true,
                        followMe = data.followMe) as T

                    showDialog()

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), data.followUserId)
                    }
                    notifyItemChanged(adapterPosition)

                    dismissDialog()
                }
                isFollowerUpdate.value = true
            }

            binding.ivOtherUserListFollowProfile.setOnClickListener{
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }

            binding.tvOtherUserListFollowNick.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }
        }
    }


    inner class TypeFollowingViewHolder(private val binding: ItemOtherUserListFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetOtherFollowingEntity) {
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
                    list[adapterPosition] = GetOtherFollowingEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = false,
                        followMe = data.followMe) as T

                   showDialog()

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), data.followUserId)
                    }

                    notifyItemChanged(adapterPosition)

                    dismissDialog()

                } else {
                    list[adapterPosition] = GetOtherFollowingEntity(
                        followUserId = data.followUserId,
                        profileImgUrl = data.profileImgUrl,
                        nickname = data.nickname,
                        ifollow = true,
                        followMe = data.followMe) as T

                    showDialog()

                    runBlocking(Dispatchers.IO){
                        FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), data.followUserId)
                    }

                    notifyItemChanged(adapterPosition)

                    dismissDialog()
                }
                isFollowingUpdate.value = true
            }

            binding.ivOtherUserListFollowProfile.setOnClickListener{
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }

            binding.tvOtherUserListFollowNick.setOnClickListener {
                if (context.javaClass.simpleName == "MainActivity"){
                    (context as MainActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }else if(context.javaClass.simpleName=="ExtraActivity"){
                    (context as ExtraActivity).moveOtherUserProfile(data.followUserId, data.nickname)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(type){
            0 -> {
                TypeFollowViewHolder(
                    ItemOtherUserListFollowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            1 -> TypeFollowingViewHolder(
                ItemOtherUserListFollowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }

    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (type) {
            0 -> {
                val followerData = list[position] as GetOtherFollowerEntity
                holder as OtherUserListRvAdapter<*>.TypeFollowViewHolder
                holder.bind(followerData)
            }
            1 -> {
                val followingData = list[position] as GetOtherFollowingEntity
                holder as OtherUserListRvAdapter<*>.TypeFollowingViewHolder
                holder.bind(followingData)
            }
            else -> throw IllegalArgumentException("알 수 없는 뷰 홀더 유형")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    private fun showDialog(){
        if (context.javaClass.simpleName == "MainActivity"){
            (context as MainActivity).showLoadingDialog(context)
        }else if(context.javaClass.simpleName=="ExtraActivity"){
            (context as ExtraActivity).showLoadingDialog(context)
        }
    }

    private fun dismissDialog(){
        if (context.javaClass.simpleName == "MainActivity"){
            (context as MainActivity).dismissLoadingDialog()
        }else if(context.javaClass.simpleName=="ExtraActivity"){
            (context as ExtraActivity).dismissLoadingDialog()
        }
    }

}

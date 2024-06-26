package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.databinding.ItemReviewReplyBinding
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs


class ExhibitionReplyRvAdapter(private val replyList: MutableList<GetReplyInformation>, private val context: Context) :
    RecyclerView.Adapter<ExhibitionReplyRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemReviewReplyBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = replyList[position]
            binding.tvBtnDelete.visibility = View.INVISIBLE
            binding.tvUserName.text = item.user.nickname

            binding.tvDate.text = DateUtil().formatElapsedTime(item.createdDate)

            binding.tvReplyContent.text = item.contents
            binding.ibMenu.setOnClickListener {
                if(item.user.userId == encryptedPrefs.getUI()){
                    listener?.onMenuBtnClick(it, 0, item.replyId, position)
                }else{
                    listener?.onMenuBtnClick(it, 1, item.replyId, position)
                }
            }
            Glide.with(context)
                .load(item.user.profileImgUrl)
                .transform(CenterCrop(), RoundedCorners(80))
                .into(binding.ivUserProfile)

            val userLevelDrawable:Drawable? = setUserLevel(item.user.userLevel)
            if(userLevelDrawable!=null){
                Glide.with(context)
                    .load(userLevelDrawable)
                    .into(binding.ivIcUserLevel)
            }else{
                binding.ivIcUserLevel.visibility = ViewGroup.GONE
            }

            binding.ivUserProfile.setOnClickListener{
                (context as ExtraActivity).moveOtherUserProfile(item.user.userId, item.user.nickname)
            }

            binding.tvUserName.setOnClickListener {
                (context as ExtraActivity).moveOtherUserProfile(item.user.userId, item.user.nickname)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemReviewReplyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = replyList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)
    }

    interface OnItemClickListener {
        fun onMenuBtnClick(btn: View, user:Int, contentId:Int, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun removeItem(position: Int){
        replyList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun setUserLevel(level:String?): Drawable?{
        var img: Drawable? = null
        when(level){
            "BEGINNER" -> img = context.getDrawable(R.drawable.ic_user_level_1_beginner)
            "INTERMEDIATE" -> img = context.getDrawable(R.drawable.ic_user_level_2_intermediate)
            "ADVANCED" -> img = context.getDrawable(R.drawable.ic_user_level_3_advanced)
            "MASTER" -> img = context.getDrawable(R.drawable.ic_user_level_4_master)
            null -> {}
        }
        return img
    }
}
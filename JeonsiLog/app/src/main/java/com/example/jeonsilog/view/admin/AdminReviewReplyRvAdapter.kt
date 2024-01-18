package com.example.jeonsilog.view.admin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.databinding.ItemReviewReplyBinding
import com.example.jeonsilog.widget.utils.DateUtil

class AdminReviewReplyRvAdapter(private val replyList: MutableList<GetReplyInformation>, private val context: Context) :
    RecyclerView.Adapter<AdminReviewReplyRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemReviewReplyBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            Log.d("admin", "bind: ")
            val item = replyList[position]
            binding.ibMenu.visibility = View.INVISIBLE

            binding.tvUserName.text = item.user.nickname
            binding.tvDate.text = DateUtil().formatElapsedTime(item.createdDate)
            binding.tvReplyContent.text = item.contents

            binding.tvBtnDelete.visibility = View.VISIBLE
            binding.tvBtnDelete.setOnClickListener {
                listener?.onDeleteBtnClick(it, position, item.replyId)
            }
            Glide.with(context)
                .load(item.user.profileImgUrl)
                .transform(CenterCrop(), RoundedCorners(80))
                .into(binding.ivUserProfile)
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
        fun onDeleteBtnClick(btn: View, position: Int, replyId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    fun removeItem(position: Int){
        replyList.removeAt(position)
        notifyItemRemoved(position)
    }
}
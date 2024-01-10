package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.databinding.ItemReviewReplyBinding
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ExhibitionReplyRvAdapter(private val replyList: MutableList<GetReplyInformation>, private val context: Context) :
    RecyclerView.Adapter<ExhibitionReplyRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemReviewReplyBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = replyList[position]
            binding.tvUserName.text = item.user.nickname

            val newCreatedDateList = item.createdDate.split("T")
            val newCreatedDate = "${newCreatedDateList[0]} ${newCreatedDateList[1]}"

            binding.tvDate.text = getTimeGap(newCreatedDate)

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

    private fun getTimeGap(createdDate:String):String{
        Log.d("createdDate", "bind: creadtedate: $createdDate")
        val createdTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate)
        var now = System.currentTimeMillis()
        var date = Date(now)

        val diffMilliseconds = date.time - createdTime.time
        val diffMinutes = diffMilliseconds / (60 * 1000)
        val diffHours = diffMilliseconds / (60 * 60 * 1000)
        val diffDays = diffMilliseconds / (24 * 60 * 60 * 1000)

        if(diffMinutes <= 1){
            return "지금"
        }
        if(diffMinutes <= 60){
            return "${diffMinutes}분 전"
        }
        if(diffHours <= 24){
            return "${diffHours}시간 전"
        }
        if(diffDays in 1..1){
            return "어제"
        }
        if(diffDays <=7){
            return "${diffDays}일 전"
        }
        return SimpleDateFormat("MM.dd").format(createdDate)
    }

    fun removeItem(position: Int){
        replyList.removeAt(position)
        notifyItemRemoved(position)
    }
}
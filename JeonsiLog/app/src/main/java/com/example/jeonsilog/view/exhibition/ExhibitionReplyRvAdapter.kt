package com.example.jeonsilog.view.exhibition

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.databinding.ItemReviewReplyBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ExhibitionReplyRvAdapter(private val replyList: List<GetReplyInformation>) :
    RecyclerView.Adapter<ExhibitionReplyRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemReviewReplyBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: GetReplyInformation){
            binding.tvUserName.text = item.user.nickname

            val newCreatedDateList = item.createdDate.split("T")
            val newCreatedDate = "${newCreatedDateList[0]} ${newCreatedDateList[1]}"

            binding.tvDate.text = getTimeGap(newCreatedDate)

            binding.tvReplyContent.text = item.contents
            binding.ibMenu.setOnClickListener {
                listener?.onMenuBtnClick(it)
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
        holder.bind(replyList[position])
    }

    interface OnItemClickListener {
        fun onMenuBtnClick(btn: View)
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
}
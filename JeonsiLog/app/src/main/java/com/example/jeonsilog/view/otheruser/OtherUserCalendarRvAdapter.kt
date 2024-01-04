package com.example.jeonsilog.view.otheruser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.databinding.ItemOtherUserCalendarCellBinding
import com.example.jeonsilog.viewmodel.OtherUserCalendarViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import java.time.format.DateTimeFormatter

class OtherUserCalendarRvAdapter(private val viewModel: OtherUserCalendarViewModel) : RecyclerView.Adapter<OtherUserCalendarRvAdapter.CalendarViewHolder>() {
    private var width = 0
    private var height = 0
    inner class CalendarViewHolder(val binding: ItemOtherUserCalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetPhotoInformation) {
            println("glide")
            GlideApp.with(binding.ivOtherUserCalendarItemDay)
                .load(data.imgUrl)
                .centerCrop()
                .into(binding.ivOtherUserCalendarItemDay)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            ItemOtherUserCalendarCellBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = viewModel.dayList.value!!.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        if(width != 0 && height != 0){
            holder.itemView.layoutParams.width = this.width
            holder.itemView.layoutParams.height = this.height
        }

        val day = viewModel.dayList.value?.get(position)

        if (day == null) {
            holder.binding.tvOtherUserCalendarItemDay.text = ""
        } else {
            holder.binding.tvOtherUserCalendarItemDay.text = day.dayOfMonth.toString()

            val temp = viewModel.imageList.value
            if (temp != null) {
                for (i in temp) {
                    if (day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) != i.date) {
                        holder.binding.tvOtherUserCalendarItemDay.setTextColor(holder.itemView.context.getColor(R.color.basic_black))
                    } else {
                        holder.binding.tvOtherUserCalendarItemDay.setTextColor(holder.itemView.context.getColor(R.color.transparent))
                        holder.bind(i)
                        break
                    }
                }
            }
        }
    }

    fun setLength(width: Int){
        this.width = width
        this.height = (width * 1.1724).toInt()
    }
}
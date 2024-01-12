package com.example.jeonsilog.view.photocalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.widget.utils.GlideApp
import java.time.LocalDate
import java.time.Month


class PhotoCalendatAdapter(private val dayList: ArrayList<String>,
                           private val onItemListener: OnItemListener,
                           private val context: Context,
                           private val list: List<GetPhotoInformation>,
                            private val selectedDate:LocalDate):
    RecyclerView.Adapter<PhotoCalendatAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val dayText: TextView = itemView.findViewById(R.id.tv_day)
        val dayImg: ImageView = itemView.findViewById(R.id.iv_day)
    }

    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_cell, parent, false)
        return ItemViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


        var originalString=dayList[holder.adapterPosition]
        val dayOfMonth = originalString.padStart(2, '0')

        // list에서 mydate의 "dd"와 동일한 date를 가진 첫 번째 요소의 인덱스 찾기
        if (list!!.size!=0){

            val matchingIndex = list.indexOfFirst { it.date.substring(8) == dayOfMonth }
            if (matchingIndex != -1) {
                holder.dayImg.isVisible=true
                var imgUrl=list[matchingIndex].imgUrl
                GlideApp.with(context)
                    .load(imgUrl)
                    .transform(CenterCrop())
                    .into(holder.dayImg)
            } else {
                holder.dayImg.isVisible=false
            }
        }else{
            holder.dayImg.isVisible=false
        }

        holder.dayText.text = dayList[holder.adapterPosition]
        //날짜 변수에 담기
        //날짜 클릭 이벤트

        holder.itemView.setOnClickListener {
            if (dayList[holder.adapterPosition]!=""){
                var year=selectedDate.year
                var month=selectedDate.month
                var originalString=dayList[holder.adapterPosition]
                val dayOfMonth = originalString.padStart(2, '0')
                val itemDate = LocalDate.of(year, Month.valueOf(month.toString()), dayOfMonth.toInt())
                onItemListener.onItemClick(itemDate)
            }
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }



}

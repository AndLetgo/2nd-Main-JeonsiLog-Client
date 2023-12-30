package com.example.jeonsilog.view.photocalendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.PhotoCalendarItem
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel


class PhotoCalendatAdapter(private val dayList: ArrayList<String>,
                           private val onItemListener: OnItemListener,
                           private val viewModel: PhotoCalendarViewModel,
                            private val context: Context,
                           var yearMonth: String):
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
        dayList[holder.adapterPosition]
        var itemList = viewModel.photoCalendarItemList.value.orEmpty()
        itemList = itemList.sortedBy { it.Img_Position }
        var size=itemList.size

        if (size!=0){
            val isPositionExists = itemList.any { it.Img_Position == yearMonth+dayList[holder.adapterPosition] }
            if (isPositionExists){
                val matchingIndex = findMatchingIndex(itemList, position)
                if(matchingIndex!=null){
                    var imgRes=itemList[matchingIndex].Img_Url
                    val unsplashUrl = imgRes
                    Glide.with(context)
                        .load(unsplashUrl)
                        .transform(CenterCrop())
                        .into(holder.dayImg)
                }
                holder.dayImg.isGone=false

            }else{
                holder.dayImg.isGone=true
            }
        }else{
            holder.dayImg.isGone=true
        }


        holder.dayText.text = dayList[holder.adapterPosition]
        //날짜 변수에 담기
        var day = dayList[holder.adapterPosition]
        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener {
            //인터페이스를 통해 날짜를 넘겨준다.
            onItemListener.onItemClick(day)
        }
    }
    fun findMatchingIndex(itemList: List<PhotoCalendarItem>, position: Int): Int? {
        return itemList.indexOfFirst { it.Img_Position == yearMonth+dayList[position] }
    }
    override fun getItemCount(): Int {
        return dayList.size
    }
}

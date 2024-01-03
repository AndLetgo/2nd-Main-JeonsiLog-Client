package com.example.jeonsilog.view.photocalendar

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.PhotoCalendarItem
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel


class LoadPageRvAdapter(private val context: Context, private val items: List<Test_Item>,private val viewModel: PhotoCalendarViewModel, private val onItemListener: OnItemListener02,var cellDate:String) : RecyclerView.Adapter<LoadPageRvAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exhibitionnameTextView: TextView = view.findViewById(R.id.tv_title)
        val exhibitionlocationTextView: TextView = view.findViewById(R.id.tv_address)
        val exhibitionplaceTextView: TextView = view.findViewById(R.id.tv_place)
        val exhibitiondateTextView: TextView = view.findViewById(R.id.tv_keyword_first)
        val exhibitionpriceTextView: TextView = view.findViewById(R.id.tv_keyword_second)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_exhibition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //셀 이미지 관련 처리
        val item = items[position]
        val radiusDp = 4f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context.resources.displayMetrics).toInt()
        val unsplashUrl =item.exhibitionImg
        Glide.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(holder.itemView.findViewById(R.id.iv_poster))
        //셀 텍스트 관련 처리
        holder.exhibitionnameTextView.text = item.exhibitionName
        holder.exhibitionlocationTextView.text = item.exhibitionLocation
        holder.exhibitionplaceTextView.text = item.exhibitionPlace
        if (item.exhibitionPrice==null){
            holder.exhibitiondateTextView.isGone=true
        }else{
            holder.exhibitiondateTextView.text = item.exhibitionPrice
        }
        if (item.exhibitionDate==null){
            holder.exhibitionpriceTextView.isGone=true
        }else{
            holder.exhibitionpriceTextView.text = item.exhibitionDate
        }
        //셀아이템 터치 관련 처리
        holder.itemView.setOnClickListener{
            val item = PhotoCalendarItem(cellDate,(item.exhibitionImg))
            viewModel.addItemToPhotoCalendarList(item)
            onItemListener.onItemClick()
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}
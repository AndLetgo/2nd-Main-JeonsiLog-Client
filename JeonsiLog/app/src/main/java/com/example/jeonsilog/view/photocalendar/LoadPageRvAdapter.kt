package com.example.jeonsilog.view.photocalendar

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromPosterRequest

import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchPlaceEntity
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LoadPageRvAdapter(
    private val context: Context, private val list:List <SearchInformationEntity>,private var selectedMonth: LocalDate,private val listener: CommunicationListener,private val dialog: LoadPageDialog)
    : RecyclerView.Adapter<LoadPageRvAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exhibitionnameTextView: TextView = view.findViewById(R.id.tv_title)
        val exhibitionlocationTextView: TextView = view.findViewById(R.id.tv_address)
        val exhibitionplaceTextView: TextView = view.findViewById(R.id.tv_place)
        val exhibitiondateTextViewBefore: TextView = view.findViewById(R.id.tv_keyword_operating_before)
        val exhibitiondateTextViewIng: TextView = view.findViewById(R.id.tv_keyword_operating_ing)
        val exhibitionpriceTextViewFree: TextView = view.findViewById(R.id.tv_keyword_price_free)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exhibition_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("djakdkjaskjkdj", "$list: ")
        val unsplashUrl = list[position].imageUrl
        val radiusDp = 4f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context.resources.displayMetrics).toInt()
        GlideApp.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(holder.itemView.findViewById(R.id.iv_poster))

        holder.exhibitionnameTextView.text =list[position].exhibitionName

        if (list[position].place.placeAddress!=null){
            val words = list[position].place.placeAddress!!.split(" ")
            // 앞의 2개의 단어 추출
            if (words.size >= 2) {
                val firstWord = words[0]
                val secondWord = words[1]
                holder.exhibitionlocationTextView.text =firstWord+" "+secondWord
            }
        }
        else{
            holder.exhibitionlocationTextView.isGone=true
        }

        holder.exhibitionplaceTextView.text = list[position].place.placeName


        if (list[position].operatingKeyword=="AFTER_DISPLAY"){
            holder.exhibitiondateTextViewBefore.isGone=true
            holder.exhibitiondateTextViewIng.isGone=true

        }else if (list[position].operatingKeyword=="ON_DISPLAY"){
            holder.exhibitiondateTextViewBefore.isGone=false
            holder.exhibitiondateTextViewIng.isGone=true

        }else{
            holder.exhibitiondateTextViewBefore.isGone=true
            holder.exhibitiondateTextViewIng.isGone=false

        }
        holder.exhibitionpriceTextViewFree.isGone = list[position].priceKeyword=="PAY"

        //셀아이템 터치 관련 처리
        holder.itemView.setOnClickListener{
            //현재 날짜 : selectedDate
            //처리해야할것 01 : 존재하지 않으면 그냥 추가
            runBlocking(Dispatchers.IO) {
                val body= PostPhotoFromPosterRequest(monthYearFromDate(selectedMonth),list[position].imageUrl)
                val response = CalendarRepositoryImpl().postPhotoFromPoster(encryptedPrefs.getAT(),body)
                Log.d("tag", "$response")
                if(response.isSuccessful && response.body()!!.check){
                    Log.d("Upload", "Image uploaded successfully")

                } else {
                    Log.e("Upload", "Image upload failed")
                }
            }
            listener.onRecyclerViewItemClick(position)
            dialog?.dismiss()
        }

    }


    private fun monthYearFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }
    override fun getItemCount(): Int {
        return list.size
    }

}
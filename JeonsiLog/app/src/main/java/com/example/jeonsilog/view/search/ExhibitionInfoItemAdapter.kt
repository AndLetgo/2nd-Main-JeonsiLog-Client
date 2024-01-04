package com.example.jeonsilog.view.search

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchResponse
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.google.android.material.internal.MultiViewUpdateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExhibitionInfoItemAdapter(private val context: Context,private val edittext:String, private val list:MutableList <SearchInformationEntity>) : RecyclerView.Adapter<ExhibitionInfoItemAdapter.ViewHolder>() {
    var itemPage=0

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
        val item = list[position]
        //list.size가 position 보다 커야함
        //list의 마지막요소가 아닌 마지막 전요소에서 비교
        if (list.size>5){
            if (list.size-4==position){
                itemPage+=1
                runBlocking(Dispatchers.IO) {
                    val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,itemPage)
                    if(response.isSuccessful && response.body()!!.check){
                        val searchExhibitionResponse = response.body()
                        list.addAll(searchExhibitionResponse?.informationEntity!!.toMutableList())
                    }
                }
            }
        }
        val unsplashUrl = item.imageUrl
        val radiusDp = 4f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context.resources.displayMetrics).toInt()
        GlideApp.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(holder.itemView.findViewById(R.id.iv_poster))
        holder.exhibitionnameTextView.text =item.exhibitionName
        if (item.place.placeAddress!=null){
            val words = item.place.placeAddress.split(" ")
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

        holder.exhibitionplaceTextView.text = item.place.placeName


        if (item.operatingKeyword=="AFTER_DISPLAY"){
            holder.exhibitiondateTextViewBefore.isGone=true
            holder.exhibitiondateTextViewIng.isGone=true
            Log.d("TAG", "$item.operatingKeyword: ")
        }else if (item.operatingKeyword=="ON_DISPLAY"){
            holder.exhibitiondateTextViewBefore.isGone=false
            holder.exhibitiondateTextViewIng.isGone=true
            Log.d("TAG", "$item.operatingKeyword: ")
        }else{
            holder.exhibitiondateTextViewBefore.isGone=true
            holder.exhibitiondateTextViewIng.isGone=false
            Log.d("TAG", "$item.operatingKeyword: ")
        }
        holder.exhibitionpriceTextViewFree.isGone = item.priceKeyword=="PAY"
        holder.itemView.setOnClickListener {
            //@@클릭처리
            //전시회 id
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}

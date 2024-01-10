package com.example.jeonsilog.view.search

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchResponse
import com.example.jeonsilog.databinding.ItemExhibitionInfoBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.view.MainActivity
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

    class ViewHolder(private val binding: ItemExhibitionInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val exhibitionnameTextView: TextView = binding.tvTitle
        val exhibitionlocationTextView: TextView = binding.tvAddress
        val exhibitionplaceTextView: TextView = binding.tvPlace
        val exhibitiondateTextViewBefore: TextView = binding.tvKeywordOperatingBefore
        val exhibitiondateTextViewIng: TextView = binding.tvKeywordOperatingIng
        val exhibitionpriceTextViewFree: TextView = binding.tvKeywordPriceFree
        val posterImageView: ImageView = binding.ivPoster
        val marginFirst=binding.sMarginFirst
        val marginSecond=binding.sMarginSecond
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExhibitionInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        //list.size가 position 보다 커야함
        //list의 마지막요소가 아닌 마지막 전요소에서 비교
        if (list.size>5){
            if (list.size-4==position){
                itemPage+=1
                runBlocking(Dispatchers.IO) {
                    var listSize=list.size
                    val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,itemPage)
                    if(response.isSuccessful && response.body()!!.check){
                        val searchExhibitionResponse = response.body()
                        list.addAll(searchExhibitionResponse?.informationEntity!!.toMutableList())
                        CoroutineScope(Dispatchers.Main).launch {
                            notifyItemRangeInserted(listSize,searchExhibitionResponse?.informationEntity.size)
                        }
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
            .into(holder.posterImageView)


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
            holder.exhibitionlocationTextView.text=""
            //holder.exhibitionlocationTextView.isVisible=false
        }
        if (item.place.placeName!=null){
            holder.exhibitionplaceTextView.text = item.place.placeName
        }
        else{
            holder.exhibitionplaceTextView.text=""
            //holder.exhibitionplaceTextView.isVisible=false
        }



        if (item.operatingKeyword=="AFTER_DISPLAY"){
            //
            holder.exhibitiondateTextViewBefore.isGone=true //시작전 키워드 없음
            holder.exhibitiondateTextViewIng.isGone=true //전시중 키워드 없음
            holder.marginFirst.isGone=true//시작전 키워드와 전시중 키워드 사이 마진 제거
        }else if (item.operatingKeyword=="ON_DISPLAY"){
            //시작전
            holder.exhibitiondateTextViewBefore.isGone=false //시작전 키워드 있음
            holder.exhibitiondateTextViewIng.isGone=true //전시중 키워드 없음
            holder.marginFirst.isGone=false//시작전 키워드와 전시중 키워드 사이 마진

        }else{
            //전시중
            holder.exhibitiondateTextViewBefore.isGone=true //시작전 키워드 없음
            holder.exhibitiondateTextViewIng.isGone=false //전시중 키워드 있음 => 마진값 변경
            holder.marginFirst.isGone=false//시작전 키워드와 전시중 키워드 사이 마진
        }
        if (item.priceKeyword=="PAY") {
            holder.marginSecond.isGone=false
            holder.exhibitionpriceTextViewFree.isGone = true
        }else{
            holder.marginSecond.isGone=true
            holder.exhibitionpriceTextViewFree.isGone = false
        }
        holder.itemView.setOnClickListener {
            //@@클릭처리
            //전시회 id
            (context as MainActivity).loadExtraActivity(0, item.exhibitionId)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}

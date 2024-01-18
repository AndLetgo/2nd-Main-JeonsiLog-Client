package com.example.jeonsilog.view.search

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.databinding.ItemExhibitionInfoBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp

class ExhibitionInfoItemAdapter(private val context: Context,private val list:List<SearchInformationEntity>)
    : RecyclerView.Adapter<ExhibitionInfoItemAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemExhibitionInfoBinding) : RecyclerView.ViewHolder(binding.root) {
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

        val unsplashUrl = list[position].imageUrl
        val radiusDp = 4f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context.resources.displayMetrics).toInt()
        GlideApp.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(holder.posterImageView)

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
            holder.exhibitionlocationTextView.text=""
            //holder.exhibitionlocationTextView.isVisible=false
        }
        if (list[position].place.placeName!=null){
            holder.exhibitionplaceTextView.text = list[position].place.placeName
        }
        else{
            holder.exhibitionplaceTextView.text=""
            //holder.exhibitionplaceTextView.isVisible=false
        }



        if (list[position].operatingKeyword=="AFTER_DISPLAY"){
            //
            holder.exhibitiondateTextViewBefore.isGone=true //시작전 키워드 없음
            holder.exhibitiondateTextViewIng.isGone=true //전시중 키워드 없음
            holder.marginFirst.isGone=true//시작전 키워드와 전시중 키워드 사이 마진 제거
        }else if (list[position].operatingKeyword=="ON_DISPLAY"){
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
        if (list[position].priceKeyword=="PAY") {
            holder.marginSecond.isGone=false
            holder.exhibitionpriceTextViewFree.isGone = true
        }else{
            holder.marginSecond.isGone=true
            holder.exhibitionpriceTextViewFree.isGone = false
        }
        holder.itemView.setOnClickListener {
            //@@클릭처리
            //전시회 id
            (context as MainActivity).loadExtraActivity(0, list[position].exhibitionId)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}

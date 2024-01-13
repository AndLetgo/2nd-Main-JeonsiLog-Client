package com.example.jeonsilog.view.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.ItemHomeExhibitionBinding
import com.example.jeonsilog.databinding.RvTitleAreaBinding
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
class HomeRvAdapter(private val homeRvList:List<ExhibitionsInfo>, private val context:Context): RecyclerView.Adapter<ViewHolder>(){
    private val tag = this.javaClass.simpleName
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemHomeExhibitionBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: ExhibitionsInfo){
            Log.d(tag, "bind: ")
            binding.tvTitle.text = item.exhibitionName

            var address = ""
            if(item.place.placeAddress != null){
                val addressList = item.place.placeAddress.split(" ")
                address = "${addressList[0]} ${addressList[1]}"
                binding.tvAddress.text = address
            }

            if(item.place.placeName !=null){
                binding.tvPlace.text = item.place.placeName
            }

            var operatingKeyword = ""
            when(item.operatingKeyword){
                "ON_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_on)
                "BEFORE_DISPLAY" -> operatingKeyword = context.getString(R.string.keyword_state_before)
            }
            var priceKeyword = ""
            when(item.priceKeyword){
                "FREE" -> priceKeyword = context.getString(R.string.keyword_free)
                else -> binding.tvKeywordSecond.visibility = View.INVISIBLE
            }

            if(operatingKeyword!=""){
                binding.tvKeywordFirst.text = operatingKeyword
                binding.tvKeywordSecond.text = priceKeyword
            }else{
                if(priceKeyword!=""){
                    binding.tvKeywordSecond.visibility = View.INVISIBLE
                    binding.tvKeywordFirst.text = priceKeyword
                }else {
                    binding.tvKeywordFirst.visibility = View.INVISIBLE
                }
            }

            if(item.imageUrl != null){
                Glide.with(context)
                    .load(item.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.ivPoster)
            }else{
                Glide.with(context)
                    .load(R.drawable.illus_empty_poster)
                    .transform(CenterInside(), RoundedCorners(16))
                    .into(binding.ivPoster)
            }

        }
    }

    class HeaderHolder(val binding:RvTitleAreaBinding):RecyclerView.ViewHolder(binding.root)
    override fun getItemViewType(position: Int): Int {
        return if(position==0){
            ITEM_VIEW_TYPE_HEADER
        }else{
            ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> HeaderHolder(
                RvTitleAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ITEM_VIEW_TYPE_ITEM -> ViewHolder(
                ItemHomeExhibitionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int = exhibitionList.size +1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderHolder -> {}
            is ViewHolder -> {
                holder.bind(homeRvList[position-1])

                if(position != RecyclerView.NO_POSITION){
                    holder.itemView.setOnClickListener {
                        listener?.onItemClick(holder.itemView, homeRvList[position-1], position)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ExhibitionModel, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ExhibitionsInfo, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

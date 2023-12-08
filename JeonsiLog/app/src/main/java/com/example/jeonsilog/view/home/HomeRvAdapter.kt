package com.example.jeonsilog.view.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.jeonsilog.databinding.ItemHomeExhibitionBinding
import com.example.jeonsilog.databinding.RvTitleAreaBinding
import com.example.jeonsilog.viewmodel.HomeRvModel
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
class HomeRvAdapter(private val homeRvList:List<HomeRvModel>): RecyclerView.Adapter<ViewHolder>(){
    private val tag = this.javaClass.simpleName
    inner class ViewHolder(val binding: ItemHomeExhibitionBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: HomeRvModel){
            Log.d(tag, "bind: ")
            binding.tvTitle.text = item.title
            binding.tvAddress.text = item.address
            binding.tvPlace.text = item.place
            var operating = ""
            var price = ""
            if(position%2 == 0){
                operating = "전시중"
                price = "무료"
                binding.tvKeywordOperating.isVisible = false
            }else{
                operating = "전시전"
                price = "유료"
            }
            binding.tvKeywordOperating.text = operating
            binding.tvKeywordPrice.text = price
        }

    }

    class HeaderHolder(val binding:RvTitleAreaBinding):RecyclerView.ViewHolder(binding.root){}
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

    override fun getItemCount(): Int = homeRvList.size +1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderHolder -> {}
            is ViewHolder -> {
                holder.bind(homeRvList[position-1])
            }
        }

    }
}

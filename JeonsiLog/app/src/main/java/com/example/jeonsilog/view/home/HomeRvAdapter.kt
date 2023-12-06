package com.example.jeonsilog.view.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemHomeExhibitionBinding
import com.example.jeonsilog.viewmodel.HomeRvModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException
import kotlin.contracts.contract

class HomeRvAdapter(private val homeRvList:List<HomeRvModel>): ListAdapter<HomeRvAdapter.DataItem,RecyclerView.ViewHolder>(HomeRvDiffCallBack()) {
    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    sealed class DataItem{
        data class exhibitionItem(val exhibition:HomeRvModel):DataItem(){
            override val id = exhibition.id
        }
        object Header:DataItem(){
            override val id = Long.MIN_VALUE
        }
        abstract val id:Long
    }
    inner class ViewHolder(private val binding: ItemHomeExhibitionBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            Log.d("TEST","bind")
            binding.tvTitle.text = "${homeRvList[position].title} ${position}"
            binding.tvAddress.text = "${homeRvList[position].address} ${position}"
            binding.tvPlace.text = "${homeRvList[position].place} ${position}"
            var operating = ""
            var price = ""
            if(position%2 == 0){
                operating = "전시중"
                price = "무료"
            }else{
                operating = "전시전"
                price = "유료"
            }
            binding.tvKeywordOperating.text = operating
//            binding.tvKeywordOperating.
            binding.tvKeywordPrice.text = price
        }
    }

    class HeaderHolder(view: View):RecyclerView.ViewHolder(view){
        companion object{
            fun from(parent: ViewGroup): HeaderHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.rv_title_area, parent, false)
                return HeaderHolder(view)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.exhibitionItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> HeaderHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder(
                ItemHomeExhibitionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int = homeRvList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val item = getItem(position) as DataItem.exhibitionItem
//                holder.bind(item.exhibition!!, clickListener)
                holder.bind(position)
            }
        }

    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderList(list : List<HomeRvModel>?){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map{DataItem.exhibitionItem(it)}
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }
}

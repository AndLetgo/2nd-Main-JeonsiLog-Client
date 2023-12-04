package com.example.jeonsilog.view

import android.icu.text.CaseMap.Title
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemHomeExhibitionBinding
import com.example.jeonsilog.viewmodel.HomeRvModel

class HomeRvAdapter(private val homeRvList:List<HomeRvModel>): RecyclerView.Adapter<HomeRvAdapter.RecycleViewHolder>() {
//    private val ITEM_VIEW_TYPE_HEADER = 0
//    private val ITEM_VIEW_TYPE_ITEM = 1

    inner class RecycleViewHolder(private val binding: ItemHomeExhibitionBinding):
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

    class TitleHolder(view: View):RecyclerView.ViewHolder(view){
        companion object{
            fun from(parent: ViewGroup):TitleHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.rv_title_area, parent, false)
                return TitleHolder(view)
            }
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return when()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemHomeExhibitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = homeRvList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)
    }


}


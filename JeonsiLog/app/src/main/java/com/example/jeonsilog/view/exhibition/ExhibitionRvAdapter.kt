package com.example.jeonsilog.view.exhibition

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemExhibitionReviewBinding

class ExhibitionRvAdapter(private val ReviewList:List<ReviewModel>): RecyclerView.Adapter<ExhibitionRvAdapter.RecycleViewHolder>()  {

    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvUserName.text = ReviewList[position].userId.toString()
            binding.ibMenu.setOnClickListener{
//                val wrapper = ContextThemeWrapper(it.context, R.style.popup_menu)
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_delete, popupMenu.menu)
//                popupMenu.menu.getItem(0).setActionView(R.layout.item_popup_menu)
//                //아이템 클릭 시
//                popupMenu.setOnMenuItemClickListener {itemId ->
//                    when(itemId){
//                        R.id.menu_delete -> {}
//                        else -> {}
//                    }
//                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemExhibitionReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = ReviewList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)
    }

}
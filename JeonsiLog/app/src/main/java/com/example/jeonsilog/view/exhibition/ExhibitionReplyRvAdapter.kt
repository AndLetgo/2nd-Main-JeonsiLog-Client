package com.example.jeonsilog.view.exhibition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemReviewReplyBinding

class ExhibitionReplyRvAdapter(private val replyList: List<ReplyModel>) :
    RecyclerView.Adapter<ExhibitionReplyRvAdapter.RecycleViewHolder>(){
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemReviewReplyBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: ReplyModel){
            binding.tvUserName.text = item.name + adapterPosition
            binding.tvDate.text = item.date
            binding.tvReplyContent.text = item.contents
            binding.ibMenu.setOnClickListener {
                listener?.onMenuBtnClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemReviewReplyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int = replyList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(replyList[position])
    }

    interface OnItemClickListener {
        fun onMenuBtnClick(btn: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
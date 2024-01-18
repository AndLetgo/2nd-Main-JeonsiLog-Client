package com.example.jeonsilog.view.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.exhibition.SearchByNameEntity
import com.example.jeonsilog.databinding.ItemAdminManagingHomeBinding

class AdminManagingRvAdapter(
    private val exhibitionList:MutableList<SearchByNameEntity>, private val context: Context):
    RecyclerView.Adapter<AdminManagingRvAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    private lateinit var binding: ItemAdminManagingHomeBinding
    val TAG = "managing"

    inner class ViewHolder(val binding: ItemAdminManagingHomeBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int){
            val item = exhibitionList[position]
            binding.tvOrder.text = (position+1).toString()
            binding.tvExhibitionName.text = item.exhibitionName

            binding.ibDelete.setOnClickListener {
                listener?.deleteItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAdminManagingHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = exhibitionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                val item = exhibitionList[i + 1]
                exhibitionList[i + 1] = exhibitionList[i]
                exhibitionList[i] = item
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                val item = exhibitionList[i - 1]
                exhibitionList[i - 1] = exhibitionList[i]
                exhibitionList[i] = item
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        listener?.checkItemMoved()
    }

    fun onDragFinished(){
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun deleteItem(position: Int)
        fun checkItemMoved()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
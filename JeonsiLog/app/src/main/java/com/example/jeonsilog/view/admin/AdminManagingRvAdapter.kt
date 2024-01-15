package com.example.jeonsilog.view.admin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.DialogAdminBinding
import com.example.jeonsilog.databinding.ItemAdminManagingHomeBinding
import com.example.jeonsilog.databinding.ItemAdminReportBinding
import com.example.jeonsilog.view.home.HomeRvAdapter

class AdminManagingRvAdapter(
    private val exhibitionList:MutableList<ExhibitionsInfo>, private val context: Context):
    RecyclerView.Adapter<AdminManagingRvAdapter.ViewHolder>() {
    private lateinit var binding: ItemAdminManagingHomeBinding
    val TAG = "managing"

    inner class ViewHolder(val binding: ItemAdminManagingHomeBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int){
            val item = exhibitionList[position]
            binding.tvOrder.text = (position+1).toString()
            binding.tvExhibitionName.text = item.exhibitionName
            binding.ibDelete.setOnClickListener {
                exhibitionList.removeAt(position)
                notifyItemRemoved(position)
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
    }

    fun onDragFinished(){
        notifyDataSetChanged()
    }

}
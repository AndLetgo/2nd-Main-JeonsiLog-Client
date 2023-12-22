package com.example.jeonsilog.view.exhibition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ItemExhibitionReviewBinding

class ExhibitionRvAdapter(private val reviewList:List<ReviewModel>):
    RecyclerView.Adapter<ExhibitionRvAdapter.RecycleViewHolder>()  {
    private var listener: OnItemClickListener? = null
    inner class RecycleViewHolder(private val binding: ItemExhibitionReviewBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.tvUserName.text = reviewList[position].userId.toString()
            binding.ibMenu.setOnClickListener{
                ExtraActivity().setMenuButton(it)
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

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(position)

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, reviewList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ReviewModel, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
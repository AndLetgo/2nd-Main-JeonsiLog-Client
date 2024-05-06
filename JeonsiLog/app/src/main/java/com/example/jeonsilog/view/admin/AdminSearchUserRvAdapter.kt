package com.example.jeonsilog.view.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.databinding.ItemUserSearchBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlideApp

class AdminSearchUserRvAdapter(
    var list: MutableList<SearchUserInformationEntity>, private val context: Context):
    RecyclerView.Adapter<AdminSearchUserRvAdapter.RecycleViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class RecycleViewHolder(private val binding: ItemUserSearchBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: SearchUserInformationEntity) {
            GlideApp.with(binding.ivUserProfile)
                .load(item.profileImgUrl)
                .centerCrop()
                .circleCrop()
                .into(binding.ivUserProfile)

            binding.tvUserName.text= item.nickname

            itemView.setOnClickListener {
                (context as MainActivity).moveOtherUserProfile(item.userId, item.nickname)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val binding = ItemUserSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(v: View, data: SearchInformationEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun notifyList(newList: List<SearchUserInformationEntity>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
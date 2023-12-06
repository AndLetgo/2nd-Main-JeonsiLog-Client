package com.example.jeonsilog.view.home

import androidx.recyclerview.widget.DiffUtil

class HomeRvDiffCallBack:DiffUtil.ItemCallback<HomeRvAdapter.DataItem>() {
    override fun areItemsTheSame(
        oldItem: HomeRvAdapter.DataItem,
        newItem: HomeRvAdapter.DataItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HomeRvAdapter.DataItem,
        newItem: HomeRvAdapter.DataItem
    ): Boolean {
        return oldItem == newItem
    }

}
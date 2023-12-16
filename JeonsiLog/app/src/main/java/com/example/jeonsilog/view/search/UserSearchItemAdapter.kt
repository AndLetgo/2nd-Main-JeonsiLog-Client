package com.example.jeonsilog.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.UserSearchItem


class UserSearchItemAdapter(private val context: Context, private val items: List<UserSearchItem>) : RecyclerView.Adapter<UserSearchItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.tv_user_name)
        //val userprofileImageView:ImageView=view.findViewById(R.id.iv_user_profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val unsplashUrl = "https://picsum.photos/id/${position}/200/300"
        Glide.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(R.dimen.item_48))
            .into(holder.itemView.findViewById(R.id.iv_user_profile))

        holder.usernameTextView.text = item.username

    }

    override fun getItemCount(): Int {
        return items.size
    }
}
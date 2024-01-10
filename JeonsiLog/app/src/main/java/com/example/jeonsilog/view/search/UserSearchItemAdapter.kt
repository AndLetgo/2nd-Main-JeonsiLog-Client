package com.example.jeonsilog.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class UserSearchItemAdapter(
    private val context: Context,private val list:List<SearchUserInformationEntity>)
    : RecyclerView.Adapter<UserSearchItemAdapter.ViewHolder>() {
    var itemPage=0
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.tv_user_name)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val unsplashUrl = list[position].profileImgUrl
        GlideApp.with(context)
            .load(unsplashUrl)
            .centerCrop()
            .circleCrop()
            .into(holder.itemView.findViewById(R.id.iv_user_profile))
        holder.usernameTextView.text = list[position].nickname
        holder.itemView.setOnClickListener {
            //유저 id

            holder.itemView.setOnClickListener {
                (context as MainActivity).moveOtherUserProfile(list[position].userId, list[position].nickname)

            }

        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
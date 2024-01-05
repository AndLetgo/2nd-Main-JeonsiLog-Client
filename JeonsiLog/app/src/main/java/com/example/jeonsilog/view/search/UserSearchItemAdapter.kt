package com.example.jeonsilog.view.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class UserSearchItemAdapter(
    private val context: Context,private val edittext:String, private val list:MutableList <SearchUserInformationEntity>)
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
        val item = list[position]
        //list.size가 position 보다 커야함
        //list의 마지막요소가 아닌 마지막 전요소에서 비교
        if (list.size > 5) {
            if (list.size - 4 == position) {
                itemPage += 1
                runBlocking(Dispatchers.IO) {
                    val response =
                        UserRepositoryImpl().searchUserInfo(encryptedPrefs.getAT(), edittext)
                    if (response.isSuccessful && response.body()!!.check) {
                        val searchUserResponse = response.body()
                        list.addAll(searchUserResponse?.informationEntity!!.toMutableList())
                    }
                }
            }
        }
        val unsplashUrl = item.profileImgUrl
        GlideApp.with(context)
            .load(unsplashUrl)
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.iv_user_profile))
        holder.usernameTextView.text = item.nickname
        holder.itemView.setOnClickListener {
            //유저 id

            holder.itemView.setOnClickListener {
                (context as MainActivity).moveOtherUserProfile(item.userId, item.nickname)

            }

        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
package com.example.jeonsilog.widget.utils

import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import com.example.jeonsilog.R

class DialogUtil {
    fun setMenuButton(v: View, user:Int): PopupMenu {
        val popupMenu = PopupMenu(v.context, v)
        if(user==0){
            //현재 유저 작성 글일 때 - 삭제하기
            popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_delete, popupMenu.menu)
        }else{
            //타 유저일 때 - 신고하기
            popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_report, popupMenu.menu)
        }
        popupMenu.menu.getItem(0).setActionView(R.layout.item_popup_menu)
        return popupMenu
    }
}
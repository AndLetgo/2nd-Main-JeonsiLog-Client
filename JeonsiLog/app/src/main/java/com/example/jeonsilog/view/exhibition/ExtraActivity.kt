package com.example.jeonsilog.view.exhibition

import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityExtraBinding
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class ExtraActivity : BaseActivity<ActivityExtraBinding>({ ActivityExtraBinding.inflate(it)}) {
    private val exhibitionViewModel: ExhibitionViewModel by viewModels()
    val TAG = "Dialog"
    override fun init() {
        when(extraActivityReference){
            0 -> {
                binding.fcvNavFrame.isVisible
            }
        }

    }

    fun setMenuButton(
        v: View, fragment: FragmentManager, user:Int, type:String, contentId: Int, reviewSide: Int, position: Int){
        val popupMenu = PopupMenu(v.context, v)
        if(user==0){
            //현재 유저 작성 글일 때 - 삭제하기
            popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_delete, popupMenu.menu)
        }else{
            //타 유저일 때 - 신고하기
            popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_report, popupMenu.menu)
        }
        popupMenu.menu.getItem(0).setActionView(R.layout.item_popup_menu)
        //메뉴 아이콘 클릭 시
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.menu_delete -> {
                    showCustomDialog(fragment,"삭제_$type", contentId, reviewSide, position)
                }
                else -> {
                    showCustomDialog(fragment,"신고_$type", contentId, reviewSide, position)
                }
            }
            false
        }
        popupMenu.show()
    }
    //dialog
    fun showCustomDialog(
        fragment: FragmentManager, tag:String, contentId:Int, reviewSide: Int, position:Int) {

        val customDialogFragment = DialogWithIllus(tag, contentId , reviewSide, position)
        customDialogFragment.show(fragment, tag)
    }
    fun reloadFragment(){
        val currentDestinationId = Navigation.findNavController(binding.fcvNavFrame).currentDestination?.id
        currentDestinationId?.let { findNavController(R.id.fcv_nav_frame).navigate(it) }
    }
}
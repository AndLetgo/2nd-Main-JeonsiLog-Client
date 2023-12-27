package com.example.jeonsilog.view.exhibition

import android.app.Dialog
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityExtraBinding
import com.example.jeonsilog.view.home.HomeFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class ExtraActivity : BaseActivity<ActivityExtraBinding>({ ActivityExtraBinding.inflate(it)}) {
    val TAG = "Dialog"
    override fun init() {
//        when(extraActivityReference){
//            0 -> binding.fcvNavFrame.isVisible
//        }

    }

//    fun changeFragment(fragment: Fragment){
//        supportFragmentManager.beginTransaction().replace(R.id.fcv_nav_frame, fragment).commit()
//    }

    fun setMenuButton(v: View, fragment: FragmentManager){
        val popupMenu = PopupMenu(v.context, v)
        //UserId 체크해서 menu_delete || menu_report 뭐 넣을지 정하는 코드 추가 되어야함
        popupMenu.menuInflater.inflate(R.menu.menu_exhibition_review_delete, popupMenu.menu)
        popupMenu.menu.getItem(0).setActionView(R.layout.item_popup_menu)
        //메뉴 아이콘 클릭 시
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.menu_delete -> {
                    showCustomDialog(fragment,"")
                }
                else -> {}
            }
            false
        }
        popupMenu.show()
    }

    //dialog
    private fun showCustomDialog(fragment: FragmentManager, tag:String) {
        val customDialogFragment = DialogWithIllus()
        customDialogFragment.show(fragment, "Dialog")
    }
}
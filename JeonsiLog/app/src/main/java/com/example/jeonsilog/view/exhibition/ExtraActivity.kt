package com.example.jeonsilog.view.exhibition

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityExtraBinding
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.view.otheruser.OtherUserFragment
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.extension.NetworkDialog
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.networkState

class ExtraActivity : BaseActivity<ActivityExtraBinding>({ ActivityExtraBinding.inflate(it)}) {
    private val exhibitionViewModel: ExhibitionViewModel by viewModels()
    private var networkDialog: NetworkDialog? = null
    val TAG = "Dialog"
    override fun init() {
        when(extraActivityReference){
            0 -> {
                binding.fcvNavFrame.isVisible
                exhibitionViewModel.setCurrentExhibitionIds(exhibitionId)
            }
            2 -> {
                binding.fcvNavFrame.isVisible
            }
        }

        //네트워크 옵저버
        networkState.observe(this) {
            if(!it) {
                networkDialog = if(networkDialog != null) {
                    null
                } else {
                    NetworkDialog()
                }

                networkDialog?.show(supportFragmentManager, "NetworkDialog")
            }
        }
    }
    fun refreshFragment(fragmentId: Int) {
        val navController = findNavController(binding.fcvNavFrame)
        navController.popBackStack()
        navController.navigate(fragmentId)
    }
    fun loadExtraActivity(type:Int, newTargetId:Int){
        extraActivityReference = type
        when(type){
            0 -> exhibitionId = newTargetId
            1 -> GlobalApplication.newReviewId = newTargetId
        }
        val intent = Intent(this, ExtraActivity::class.java)
        startActivity(intent)
    }
    fun moveOtherUserProfile(otherUserId: Int, otherUserNick: String){
        if(otherUserId == GlobalApplication.encryptedPrefs.getUI()){

            val fragment = MyPageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_nav_frame, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            val fragment = OtherUserFragment(otherUserId, otherUserNick)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_nav_frame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
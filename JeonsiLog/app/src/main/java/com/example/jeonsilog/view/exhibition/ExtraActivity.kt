package com.example.jeonsilog.view.exhibition

import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityExtraBinding
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class ExtraActivity : BaseActivity<ActivityExtraBinding>({ ActivityExtraBinding.inflate(it)}) {
    private val exhibitionViewModel: ExhibitionViewModel by viewModels()
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

    }
    fun refreshFragment(fragmentId: Int) {
        val navController = findNavController(binding.fcvNavFrame)
        navController.navigate(fragmentId)
    }
}
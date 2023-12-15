package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentExhibitionBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ExhibitionFragment : BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition) {
    override fun init() {
        BottomSheetBehavior.from(binding.flBottomSheet).apply {
            peekHeight = 470
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

}
package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentWritingReviewBinding

class WritingReviewFragment : BaseFragment<FragmentWritingReviewBinding>(
    R.layout.fragment_writing_review) {
    private lateinit var dialogWithIllus: DialogWithIllus
    override fun init() {
        binding.btnCancel.setOnClickListener {
            ExtraActivity().showCustomDialog(parentFragmentManager, "감상평")
        }
    }
}
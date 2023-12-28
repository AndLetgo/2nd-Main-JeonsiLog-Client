package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentExhibitionBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ExhibitionFragment : BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition) {
    private lateinit var exhibitionRvAdapter: ExhibitionRvAdapter
    override fun init() {
        //디바이스 높이값 가져오기
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels

        //바텀시트 세팅
        BottomSheetBehavior.from(binding.nsvBottomSheet).apply {
            peekHeight = (screenHeight * 0.52).toInt()
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //RecyclerView
        val list = listOf<ReviewModel>(
            ReviewModel(0,0,"address",0,""),
            ReviewModel(1,0,"address",0,""),
            ReviewModel(2,0,"address",0,""),
            ReviewModel(3,0,"address",0,""),
            ReviewModel(4,0,"address",0,""),
            ReviewModel(5,0,"address",0,""),
            ReviewModel(6,0,"address",0,"")
        )
        exhibitionRvAdapter = ExhibitionRvAdapter(list)
        binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        binding.rvExhibitionReview.layoutManager = LinearLayoutManager(this.context)

        exhibitionRvAdapter.setOnItemClickListener(object: ExhibitionRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ReviewModel, position: Int) {
                Navigation.findNavController(v).navigate(R.id.action_exhibitionFragment_to_reviewFragment)
            }

            override fun onMenuBtnClick(btn: View) {
                ExtraActivity().setMenuButton(btn, parentFragmentManager)
            }

        })

        binding.btnWritingReview.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_writingReviewFragment)
        }
    }

}
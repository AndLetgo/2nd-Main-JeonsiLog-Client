package com.example.jeonsilog.view.admin

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminExhibitionBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AdminExhibitionFragment : BaseFragment<FragmentAdminExhibitionBinding>(R.layout.fragment_admin_exhibition) {
    private lateinit var rvAdapter: AdminExhibitionReviewRvAdapter
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

        val reviewList = listOf<ReviewModel>(
            ReviewModel(0,0,"address",0,""),
            ReviewModel(1,0,"address",0,""),
            ReviewModel(2,0,"address",0,""),
            ReviewModel(3,0,"address",0,""),
            ReviewModel(4,0,"address",0,""),
            ReviewModel(5,0,"address",0,""),
            ReviewModel(6,0,"address",0,"")
        )
        rvAdapter = AdminExhibitionReviewRvAdapter(reviewList)
        binding.rvExhibitionReview.adapter = rvAdapter
        binding.rvExhibitionReview.layoutManager = LinearLayoutManager(this.context)

        rvAdapter.setOnItemClickListener(object : AdminExhibitionReviewRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ReviewModel, position: Int) {
                Navigation.findNavController(v).navigate(R.id.action_adminExhibitionFragment_to_adminReviewFragment)
            }
        })

        binding.ivPosterImage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminExhibitionFragment_to_adminPosterFragment)
        }
    }
}
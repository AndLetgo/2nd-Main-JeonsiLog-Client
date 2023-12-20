package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentExhibitionBinding
import com.example.jeonsilog.view.home.HomeRvAdapter
import com.example.jeonsilog.viewmodel.HomeRvModel
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

    }

}
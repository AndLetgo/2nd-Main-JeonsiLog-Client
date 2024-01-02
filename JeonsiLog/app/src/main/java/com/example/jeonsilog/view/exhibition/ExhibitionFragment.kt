package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionInfo
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.FragmentExhibitionBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.math.log
import kotlin.math.max

class ExhibitionFragment : BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition) {
    private lateinit var exhibitionRvAdapter: ExhibitionRvAdapter

    override fun init() {
        //페이지 세팅
        getExhibitionInfo()

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

        //감상평 작성하기
        binding.btnWritingReview.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_writingReviewFragment)
        }

        //poster page
        binding.ivPosterImage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_posterFragment)
        }

        binding.llExhibitionPlace.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_exhibitionPlaceFragment)
        }
    }

    private fun getExhibitionInfo(){
        val data: ExhibitionInfo? = runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibition(encryptedPrefs.getAT(), exhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                response.body()!!.information
            }else{
                null
            }
        }
        if(data != null){
            Log.d("exhibition", "getExhibitionInfo: ${data.imageUrl}")
            Glide.with(requireContext())
                .load(data.imageUrl)
                .into(binding.ivPosterImage)

            binding.tvExhibitionName.text = data.exhibitionName
            binding.tvAddress.text = data.place.address
            binding.tvPlaceName.text = data.place.placeName

            var date = ""
            if(data.startDate!=null){
                date = subStringDate(data.startDate) + " ~ " + subStringDate(data.endDate)
            }
            binding.tvDate.text = date

            val sampleText = "많은 화제가 되고 있는 류지안 작가의 신작과 MOONLIGHT 시리즈, HERITAGE 시리즈, THE MOON 작품을 비롯하여, 김종언 작가님의 밤새… 시리즈를 만나보실 수 있습니다. 더불어 김동우 작가님의 조각상도 함께 만나보세요."
            binding.tvInformation.text = sampleText
            var check = true
            binding.tvReadMoreInfo.setOnClickListener {
                if(check){
                    binding.tvInformation.maxLines = Int.MAX_VALUE
                    binding.tvReadMoreInfo.visibility = View.GONE
                    check = !check
                }
            }
            binding.tvInformation.setOnClickListener {
                if(!check){
                    binding.tvInformation.maxLines = 3
                    binding.tvReadMoreInfo.visibility = View.VISIBLE
                    check = !check
                }
            }
        }
    }

    private fun subStringDate(date:String):String{
        var newDate = ""
        newDate = date.substring(0,4) +"."+date.substring(4,6)+ "."+date.substring(6)
        return newDate
    }

}
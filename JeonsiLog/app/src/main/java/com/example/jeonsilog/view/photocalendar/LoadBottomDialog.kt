package com.example.jeonsilog.view.photocalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.example.jeonsilog.data.remote.dto.PhotoCalendarItem
import com.example.jeonsilog.databinding.ViewLoadDialogBinding
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LoadBottomDialog(val cellDate:String,private val viewModel: PhotoCalendarViewModel) : BottomSheetDialogFragment() {

    private var _binding: ViewLoadDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewLoadDialogBinding.inflate(inflater, container, false)

        SetDeleteImage()
        setBtLoadPoster()
        setBtLoadImage()

        return binding.root
    }
    fun SetDeleteImage(){
        //이미지 삭제하기 버튼 처리
        binding.btSetDeleteImage.isGone=true
        val isPositionExists = viewModel.photoCalendarItemList.value.orEmpty().any { it.Img_Position == cellDate }
        if (isPositionExists){
            binding.btSetDeleteImage.isGone=false
        }
        binding.btSetDeleteImage.setOnClickListener {
            viewModel.removeItemByPosition(cellDate)
            dismiss()
        }
    }
    fun setBtLoadPoster(){
        binding.btLoadPoster.setOnClickListener {
            // 기존 다이얼로그 닫기
            dismiss()
            // 새로운 다이얼로그 열기
            val loadPageDialog = LoadPageDialog(viewModel,cellDate)
            loadPageDialog.show(parentFragmentManager, "LoadPageDialogTag")
        }
    }
    fun setBtLoadImage(){
        binding.btLoadImage.setOnClickListener {
            val item = PhotoCalendarItem(cellDate,"https://picsum.photos/id/100/200/300")
            viewModel.addItemToPhotoCalendarList(item)
            dismiss()
        }
    }
}
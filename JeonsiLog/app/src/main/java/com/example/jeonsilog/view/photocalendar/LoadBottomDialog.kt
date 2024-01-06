package com.example.jeonsilog.view.photocalendar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.DeletePhotoRequest
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromGalleryRequest
import com.example.jeonsilog.data.remote.dto.calendar.UploadImageReqEntity
import com.example.jeonsilog.databinding.ViewLoadDialogBinding
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LoadBottomDialog(private var selectedDate: LocalDate, private val listener: CommunicationListener) : BottomSheetDialogFragment() {
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    val MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 2
    private var _binding: ViewLoadDialogBinding? = null
    private val binding get() = _binding!!
    var imageUri: Uri? = null
    private var dismissListener: OnDismissListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewLoadDialogBinding.inflate(inflater, container, false)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        SetDeleteImage()
        setBtLoadPoster()
        setBtLoadImage()

        return binding.root
    }
    private fun yearMonthFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyyMM")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }
    fun SetDeleteImage(){
        lateinit var  list: List<GetPhotoInformation>
        var yearMonth = yearMonthFromDate(selectedDate)
        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().getMyPhotoMonth(encryptedPrefs.getAT(),yearMonth)
            if(response.isSuccessful && response.body()!!.check){
                list= response.body()!!.information
            } else {
                list= listOf()
            }
        }
        if (!list.isNullOrEmpty()){
            if (list.size!=0){
                val parts: List<String> = selectedDate.toString().split("-")
                // 마지막 부분만 출력
                val lastPart: String = parts.last()
                val index: Int = list.indexOfFirst {
                    it.date.substring(8) == lastPart
                }
                binding.btSetDeleteImage.isGone = index == -1
            }
        }else{
            binding.btSetDeleteImage.isGone=true
        }
        //클릭설정
        binding.btSetDeleteImage.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                val body= DeletePhotoRequest(monthYearFromDate(selectedDate))
                val response = CalendarRepositoryImpl().deletePhoto(encryptedPrefs.getAT(),body)
                if(response.isSuccessful && response.body()!!.check){
                }
            }
            if (!list.isNullOrEmpty()){
                listener.onDialogButtonClick("Data to pass")
                dismiss()
            }
        }
    }
    fun setBtLoadPoster(){
        binding.btLoadPoster.setOnClickListener {
            // 기존 다이얼로그 닫기
            dismiss()
            // 새로운 다이얼로그 열기
            val loadPageDialog = LoadPageDialog(selectedDate,listener,null)
            loadPageDialog.show(parentFragmentManager, "LoadPageDialogTag")
        }
    }
    fun setBtLoadImage(){
        binding.btLoadImage.setOnClickListener {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES
                    )
                } else {
                    // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                    accessGallery()
                }
            } else{
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                } else {
                    // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                    accessGallery()
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE,
            MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여된 경우 갤러리에 접근할 수 있는 로직을 수행
                    accessGallery()
                } else {
                    // 권한이 거부된 경우 사용자에게 설명이나 다시 요청하는 등의 처리를 수행
                    // 예: Toast 메시지를 통해 사용자에게 알림
                    Toast.makeText(
                        context,
                        "갤러리 접근 권한이 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    fun accessGallery(){
        // 갤러리 열기 Intent 생성
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)

    }


    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data!!.data
            if (imageUri != null) {
                try {
                    patchMyPhotoCalendarImg(imageUri!!)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun patchMyPhotoCalendarImg(uri: Uri) {
        val file = File(ImageUtil().absolutelyPath(requireContext(), uri))
        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("img", file.name, imageRequestBody)

        val uploadImageReq=UploadImageReqEntity(monthYearFromDate(selectedDate))
        val upImageReq=PostPhotoFromGalleryRequest(uploadImageReq,file.name)
        val requestJson= Gson().toJson(upImageReq)
        val requestBody=RequestBody.create("application/json".toMediaTypeOrNull(),requestJson)
        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().postPhotoFromGallery(encryptedPrefs.getAT(),requestBody, filePart)
            if(response.isSuccessful && response.body()!!.check){
                CoroutineScope(Dispatchers.Main).launch {
                    listener.onDialogButtonClick("Data to pass")
                }
            } else {
            }
            dismiss()
            dismissListener?.onDismiss()
        }
    }
    private fun monthYearFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }
    fun setOnDismissListener(listener: OnDismissListener) {
        dismissListener = listener
    }
    interface OnDismissListener {
        fun onDismiss()
    }



}
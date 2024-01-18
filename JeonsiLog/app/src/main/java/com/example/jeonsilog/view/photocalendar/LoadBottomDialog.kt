package com.example.jeonsilog.view.photocalendar

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.DeletePhotoRequest
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.data.remote.dto.calendar.UploadImageReqEntity
import com.example.jeonsilog.databinding.ViewLoadDialogBinding
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LoadBottomDialog(private var selectedDate: LocalDate, private val listener: CommunicationListener) : DialogFragment() {
    private val MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 2


    private var _binding: ViewLoadDialogBinding? = null
    private val binding get() = _binding!!

    var imageUri: Uri? = null

    private var dismissListener: OnDismissListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewLoadDialogBinding.inflate(inflater, container, false)


        setDimClick()
        SetDeleteImage()
        setBtLoadPoster()
        setBtLoadImage()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()

    }
    private fun setView(){
        // 다이얼로그의 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그의 외부 터치 이벤트 처리 (다이얼로그가 닫히지 않도록 함)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 뒤로가기 버튼이 눌렸을 때 수행할 동작
                dismiss()
                true
            } else {
                false
            }
        }

        // 전체 화면으로 다이얼로그 표시 (다이얼로그 내용물의 크기를 외부까지 확장)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
        // 다이얼로그의 내용물이 차지하는 레이아웃의 크기를 조정
        val params = binding.root.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.root.layoutParams = params
    }
    private fun setDimClick(){
        binding.ivDimmingZone.setOnClickListener {
            dismiss()
        }
    }

    private fun yearMonthFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyyMM")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }
    private fun SetDeleteImage(){
        lateinit var  list: List<GetPhotoInformation>
        var yearMonth = yearMonthFromDate(selectedDate)
        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().getMyPhotoMonth(encryptedPrefs.getAT(),yearMonth)
            if(response.isSuccessful && response.body()!!.check){
                list= response.body()!!.information
            } else {
                list= response.body()!!.information
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
                val body= DeletePhotoRequest(DateUtil().monthYearFromDate(selectedDate))
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
    private fun setBtLoadPoster(){
        binding.btLoadPoster.setOnClickListener {
            // 기존 다이얼로그 닫기
            dismiss()
            // 새로운 다이얼로그 열기
            val loadPageDialog = LoadPageDialog(selectedDate,listener,"")
            loadPageDialog.show(parentFragmentManager, "LoadPageDialogTag")
        }
    }
    private fun setBtLoadImage() {
        binding.btLoadImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }
    }
    private fun checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                requestStoragePermission()
                Log.d("TAG01", "checkPermissionAndOpenGallery: ")
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                accessGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                requestStoragePermission()

            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                accessGallery()
            }
        }

    }
    private fun requestStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                // 권한을 이전에 거부한 경우
                showPermissionRationaleDialog()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES
                )
            }
        }else{

        }
    }
    private fun showPermissionRationaleDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.permission_denied))
            .setPositiveButton("이동") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun openAppSettings() {
        Log.d("openAppSettings", "openAppSettings: ")
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = android.net.Uri.parse("package:" +requireContext() .packageName)
        startActivity(intent)
    }


    private fun accessGallery(){
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

        val uploadImageReq = UploadImageReqEntity(DateUtil().monthYearFromDate(selectedDate))
        val requestJson = Gson().toJson(uploadImageReq)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(),requestJson)

        runBlocking(Dispatchers.IO) {
            val response = CalendarRepositoryImpl().postPhotoFromGallery(encryptedPrefs.getAT(), requestBody, filePart)
            if(response.isSuccessful && response.body()!!.check){
                CoroutineScope(Dispatchers.Main).launch {
                    listener.onDialogButtonClick("Data to pass")
                }
                Log.d("gallery", "patchMyPhotoCalendarImg: ${response.body()!!.informationEntity.message}")
            }
            dismiss()
            dismissListener?.onDismiss()
        }
    }

    fun setOnDismissListener(listener: OnDismissListener) {
        dismissListener = listener
    }
    interface OnDismissListener {
        fun onDismiss()
    }
}

package com.example.jeonsilog.view.exhibition

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.databinding.FragmentPosterBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.report.ReportRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PosterFragment : BaseFragment<FragmentPosterBinding>(
    R.layout.fragment_poster) {
    private var posterList:MutableList<String>? = null
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private val TAG = "download"
    private var thisExhibitionId = 0
    private var thisPosterUrl = ""

    override fun init() {
        thisExhibitionId = exhibitionViewModel.currentExhibitionIds.value!![exhibitionViewModel.currentExhibitionIds.value!!.size-1]
        posterList = mutableListOf()

        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getPoster(encryptedPrefs.getAT(), thisExhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                thisPosterUrl =response.body()!!.information.imageUrl
            }else{
                null
            }
        }
        if(thisPosterUrl != ""){
            Glide.with(requireContext())
                .load(thisPosterUrl)
                .transform(CenterInside())
                .into(binding.ivPoster)
            binding.llPosterEmptyState.visibility = View.GONE
        }else{
            binding.llPosterEmptyState.visibility = View.VISIBLE
            binding.ivPoster.visibility = View.GONE
            binding.ibDownload.visibility = View.GONE
        }

        //empty poster 신고
        binding.btnReportPosterEmpty.setOnClickListener {
            var isSuccess = false
            runBlocking(Dispatchers.IO){
                val body = PostReportRequest("POSTER", thisExhibitionId)
                val response = ReportRepositoryImpl().postReport(encryptedPrefs.getAT(), body)
                if(response.isSuccessful && response.body()!!.check){
                    isSuccess = true
                }
            }
            if(isSuccess){
                Toast.makeText(requireContext(), getString(R.string.toast_poster_report_success), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), getString(R.string.toast_poster_report_failure), Toast.LENGTH_SHORT).show()
            }
        }

        //이미지 다운로드
        binding.ibDownload.setOnClickListener {
            Log.d(TAG, "init: click button")
            checkStoragePermission()
        }
    }

    //이미지 다운로드
    private fun checkStoragePermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                Log.d(TAG, "checkStoragePermission: 권한 없음")
                showPermissionRationale(getString(R.string.permission_denied))
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                Log.d(TAG, "checkStoragePermission: 권한 있음")
                useDownloadManager(thisPosterUrl)
            }
        } else{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                Log.d(TAG, "checkStoragePermission: 33이하, 권한 없음")
                showPermissionRationale(getString(R.string.permission_denied))
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                Log.d(TAG, "checkStoragePermission: 33이하, 권한 있음")
                useDownloadManager(thisPosterUrl)
            }
        }
    }
    private fun showPermissionRationale(msg: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("확인") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialog.setNegativeButton("취소") { _, _ ->

        }
        alertDialog.show()
    }
    private fun useDownloadManager(imageUrl: String){
        val uri = Uri.parse(imageUrl)
        val fileName = uri.lastPathSegment
        val request = DownloadManager.Request(uri)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(requireContext(), "저장 성공!", Toast.LENGTH_SHORT).show()
    }
}
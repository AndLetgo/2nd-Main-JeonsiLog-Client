package com.example.jeonsilog.view.exhibition

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.databinding.FragmentPosterBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.report.ReportRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionPosterViewModel
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class PosterFragment : BaseFragment<FragmentPosterBinding>(
    R.layout.fragment_poster) {
    private var posterList:MutableList<String>? = null
    private val exhibitionPosterViewModel:ExhibitionPosterViewModel by viewModels()
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager
    private val TAG = "download"

    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 500
//    val MY_PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 4

    override fun init() {
        viewPager = binding.vpPoster

        posterList = mutableListOf()

        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getPoster(encryptedPrefs.getAT(), exhibitionViewModel.currentExhibitionId.value!!)
            if(response.isSuccessful && response.body()!!.check){
                posterList?.add(response.body()!!.information.imageUrl)
                binding.llPosterEmptyState.visibility = View.GONE
            }else{
                binding.llPosterEmptyState.visibility = View.VISIBLE
                binding.vpPoster.visibility = View.GONE
                binding.ibDownload.visibility = View.GONE
                null
            }
        }
        Log.d("poster", "init: posterlist size: ${posterList?.size}")
        exhibitionPosterViewModel.setMaxCount(posterList?.size.toString())
        val adapter = posterList?.let { PosterVpAdapter(it, requireContext()) }
        adapter?.setCountListener(object : PosterVpAdapter.CountListener{
            override fun setCount(position: Int) {
                exhibitionPosterViewModel.setCount((position+1).toString())
            }
        })
        adapter?.setOnPageChangeListener(viewPager)
        binding.vpPoster.adapter = adapter

        //empty poster 신고
        binding.btnReportPosterEmpty.setOnClickListener {
            runBlocking(Dispatchers.IO){
                val body = PostReportRequest("POSTER", exhibitionViewModel.currentExhibitionId.value!!)
                ReportRepositoryImpl().postReport(encryptedPrefs.getAT(), body)
            }
        }

        //이미지 다운로드
        binding.ibDownload.setOnClickListener {
            Log.d(TAG, "init: click button")
            checkStoragePermission()
        }
    }

    //이미지 다운로드
    private fun imageUrlToBitmap(imageUrl:String):Bitmap?{
        try{
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            return bitmap
        }catch (e:IOException){
            Log.e("poster", "imageUrlToBitmap: $e", )
        }
        return null
    }
    private fun checkStoragePermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                Log.d(TAG, "checkStoragePermission: 권한 없음")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                Log.d(TAG, "checkStoragePermission: 권한 있음")
                var bitmap:Bitmap?
                lifecycleScope.launch(Dispatchers.IO){
                    bitmap = imageUrlToBitmap(posterList!![0])
                    downloadImage(bitmap!!)
                }
            }
        } else{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 권한 요청 다이얼로그를 표시
                Log.d(TAG, "checkStoragePermission: 33이하, 권한 없음")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                // 권한이 이미 있는 경우 갤러리에 접근할 수 있는 로직을 수행
                Log.d(TAG, "checkStoragePermission: 33이하, 권한 있음")
                var bitmap:Bitmap?
                lifecycleScope.launch(Dispatchers.IO){
                    bitmap = imageUrlToBitmap(posterList!![0])
                    downloadImage(bitmap!!)
                }
            }
        }
    }
    private fun downloadImage(bitmap: Bitmap) {
        Log.d(TAG, "downloadImage: 실행됨")
        val fileName = System.currentTimeMillis().toString()+".png"

        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val directory = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "DownloadedImages")
        if(!directory.exists()){
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            Toast.makeText(requireContext(), "포스터를 저장했습니다.", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }
}
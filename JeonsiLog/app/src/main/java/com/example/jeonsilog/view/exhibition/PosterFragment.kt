package com.example.jeonsilog.view.exhibition

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentPosterBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionPosterViewModel
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
import java.net.HttpURLConnection
import java.net.URL

class PosterFragment : BaseFragment<FragmentPosterBinding>(
    R.layout.fragment_poster) {
    private var posterList:MutableList<String>? = null
    private val viewModel:ExhibitionPosterViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    override fun init() {
        viewPager = binding.vpPoster

        posterList = mutableListOf()
//        val poster =
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getPoster(encryptedPrefs.getAT(), exhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                Log.d("poster", "init: response.body()!!.information.imageUrl: ${response.body()!!.information.imageUrl}")
                posterList?.add(response.body()!!.information.imageUrl)
                Log.d("poster", "init: posterlist size: ${posterList?.size}")
            }else{
                null
            }
        }
        Log.d("poster", "init: posterlist size: ${posterList?.size}")
        viewModel.setMaxCount(posterList?.size.toString())
        val adapter = posterList?.let { PosterVpAdapter(it, requireContext()) }
        adapter?.setCountListener(object : PosterVpAdapter.CountListener{
            override fun setCount(position: Int) {
                viewModel.setCount((position+1).toString())
            }
        })
        adapter?.setOnPageChangeListener(viewPager)
        binding.vpPoster.adapter = adapter

        binding.vm = viewModel
        viewModel.count.observe(this){
            binding.tvCountPoster.text = it
        }

        binding.ibBack.setOnClickListener{
            val currentIndex = viewPager.currentItem
            if(currentIndex > 0 ){
                viewPager.setCurrentItem(currentIndex-1,true)
            }
        }
        binding.ibNext.setOnClickListener{
            val currentIndex = viewPager.currentItem
            if(currentIndex < posterList!!.size){
                viewPager.setCurrentItem(currentIndex+1,true)
            }
        }

        //이미지 다운로드
        binding.ibDownload.setOnClickListener {
            val image = this.posterList!![viewPager.currentItem]
            Log.d("download", "init: imageurl: $image")
            lifecycleScope.launch(Dispatchers.IO) {
                val bitmap = downloadImageToBitmap(image)
                if(bitmap != null){
                    downloadImage(bitmap)
                }
            }
        }
    }

    //이미지 다운로드
    private fun downloadImageToBitmap(imageUrl:String):Bitmap?{
        try{
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            Log.d("download", "downloadImageToBitmap: bitmap: $bitmap")
            return bitmap
        }catch (e:IOException){
            Log.e("download", "downloadImageToBitmap: $e", )
        }
        return null
    }
    private fun checkStoragePermission() {

    }
    private fun downloadImage(bitmap: Bitmap) {

        val fileOutputStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = requireContext().contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fileOutputStream = imageUri?.let { resolver.openOutputStream(it) } ?: return
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, "image_${System.currentTimeMillis()}.jpg")
            fileOutputStream = FileOutputStream(image)
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        Toast.makeText(requireContext(), "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    //접근 권한
    private fun requestPermission(){

    }

}
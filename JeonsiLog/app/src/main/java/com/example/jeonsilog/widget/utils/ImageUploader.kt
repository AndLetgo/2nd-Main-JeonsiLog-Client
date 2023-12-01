package com.example.jeonsilog.widget.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.jeonsilog.repository.ImageRepositoryImpl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ImageUploader {

    private fun absolutelyPath(uri: Uri, context : Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath ?: ""
    }

    suspend fun uploadImageToServer(selectedImageUri: Uri?, context: Context) {

        if(selectedImageUri != null){
            val file = File(absolutelyPath(selectedImageUri, context))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            ImageRepositoryImpl().postProfileImage(body)
        } else {
            Log.d("Upload", "selectedImageUri is null" )
        }
    }
}
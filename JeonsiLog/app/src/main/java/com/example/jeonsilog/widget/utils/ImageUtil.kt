package com.example.jeonsilog.widget.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class ImageUtil {

    fun absolutelyPath(context: Context, path: Uri): String{
        val result: String
        val c: Cursor? = context.contentResolver.query(path, null, null, null, null)
        result = if(c == null){
            path.path.toString()
        } else {
            c.moveToFirst()
            val index = c.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            c.getString(index)
        }
        c?.close()
        return result
    }
}
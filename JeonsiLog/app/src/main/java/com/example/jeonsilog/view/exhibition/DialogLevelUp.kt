package com.example.jeonsilog.view.exhibition

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogLevelUpBinding

class DialogLevelUp(context: Context, private val userLevel:String): Dialog(context) {
    private lateinit var binding: DialogLevelUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLevelUpBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)

        resize(this, 0.8f, 0.32f)

        if(userLevel=="NON"){
            binding.content = context.getString(R.string.dialog_level_up_first)
            binding.btnText = context.getString(R.string.btn_confirm)
        }else{
            binding.content = context.getString(R.string.dialog_level_up, userLevel)
            binding.btnText = context.getString(R.string.btn_close)
        }

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }

    private fun resize(dialog: Dialog, width: Float, height: Float){
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val size = Point()
            windowManager.defaultDisplay.getSize(size)

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            dialog.window?.setLayout(x, y)
        } else {
            val rect = windowManager.currentWindowMetrics.bounds

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()
            dialog.window?.setLayout(x, y)
        }
    }
}
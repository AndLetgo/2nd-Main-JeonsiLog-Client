package com.example.jeonsilog.widget.extension

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.example.jeonsilog.databinding.DialogLodingBinding

class LoadingDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogLodingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    private fun init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLodingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.2f)
    }
    override fun show() {
        if(!this.isShowing) super.show()
    }
}
package com.example.jeonsilog.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.jeonsilog.widget.extension.LoadingDialog

abstract class BaseActivity<B : ViewBinding>(
    private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity(){
    protected lateinit var binding: B
        private set

    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    protected abstract fun init()

    fun showLoadingDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog.show()
    }
    fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }
}
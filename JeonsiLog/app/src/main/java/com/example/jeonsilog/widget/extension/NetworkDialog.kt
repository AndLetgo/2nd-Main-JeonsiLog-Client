package com.example.jeonsilog.widget.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogNetworkBinding
import com.example.jeonsilog.widget.extension.NetworkManager.Companion.checkNetworkState
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.globalContext
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh

class NetworkDialog: DialogFragment() {
    private var _binding: DialogNetworkBinding? = null
    private val binding get() = _binding!!
    private var cancelPressedTime: Long = 0L

    override fun onStart() {
        super.onStart()

        val widthInDp = 324

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_network, container, false)
        binding.lifecycleOwner = this

        binding.btnDialogNetworkCancel.setOnClickListener {
            if(checkNetworkState(requireContext())){
                isRefresh.value = true
                dismiss()
            } else {
                if(System.currentTimeMillis() - cancelPressedTime <= 2000){
                    globalContext.exitApp()
                } else {
                    cancelPressedTime = System.currentTimeMillis()
                    Toast.makeText(globalContext, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDialogNetworkRefresh.setOnClickListener {
            if(checkNetworkState(requireContext())){
                isRefresh.value = true
                dismiss()
            } else {
                Toast.makeText(globalContext, "네트워크 설정을 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.jeonsilog.fcm.services


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogFcmBinding


class FcmDialog: DialogFragment() {
    private var _binding: DialogFcmBinding? = null
    private val binding get() = _binding!!

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fcm, container, false)
        binding.lifecycleOwner = this

        binding.btnDialogFcmClose.setOnClickListener {
            dismiss()
        }

        binding.btnDialogFcmSetting.setOnClickListener {
            openAppSettings()
        }
        return binding.root
    }
    private fun openAppSettings() {
        Log.d("openAppSettings", "openAppSettings: ")
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = android.net.Uri.parse("package:" +requireContext() .packageName)
        startActivity(intent)
        dismiss()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
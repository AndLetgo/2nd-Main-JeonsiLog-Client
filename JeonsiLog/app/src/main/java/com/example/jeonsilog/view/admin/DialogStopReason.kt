package com.example.jeonsilog.view.admin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogStopReasonBinding

class DialogStopReason(private val userId: Int): DialogFragment() {
    private var _binding: DialogStopReasonBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 324
        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val heightDp = 228
        val heightPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, heightDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, heightPixels)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_stop_reason, container, false)

        binding.btnDone.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {

        }

        return binding.root
    }
}
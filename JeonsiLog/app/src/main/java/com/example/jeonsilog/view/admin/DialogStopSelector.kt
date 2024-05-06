package com.example.jeonsilog.view.admin

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
import com.example.jeonsilog.data.remote.dto.stop.StopReq
import com.example.jeonsilog.databinding.DialogStopSelectorBinding
import com.example.jeonsilog.repository.stop.StopRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogStopSelector(private val userId: Int): DialogFragment() {
    private var _binding: DialogStopSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 324
        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val heightDp = 352
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_stop_selector, container, false)

        binding.btnDone.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            if(binding.rbEtc.isChecked) {
                DialogStopReason(userId).show(requireActivity().supportFragmentManager, "Reason_Dialog")
                dismiss()
            }
            else stopUser()
        }

        return binding.root
    }

    private fun stopUser(){
        CoroutineScope(Dispatchers.IO).launch {
            val res = StopRepositoryImpl().stopUser(encryptedPrefs.getAT(), StopReq(userId = userId, reason = "부적절한 언행 사용"))
            if(res.isSuccessful && res.body()!!.check) dismiss() else Toast.makeText(requireContext(), "정지 요청 실패", Toast.LENGTH_SHORT).show()
        }
    }
}
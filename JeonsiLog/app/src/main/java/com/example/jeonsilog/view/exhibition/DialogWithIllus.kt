package com.example.jeonsilog.view.exhibition

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogWithIllusBinding

class DialogWithIllus(private val type:String): DialogFragment(){
    private var _binding: DialogWithIllusBinding? = null
    private val binding get() = _binding!!
    val TAG = "Dialog"

    override fun onStart() {
        super.onStart()

        val widthInDp = 324
        val heightInDp = 242

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val heightInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_with_illus, container, false)
//        binding.lifecycleOwner = this

        when(type){
            "신고_감상평" -> {
                binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_report) })
                binding.tvDialogMessage.text = getString(R.string.dialog_report, "감상평")
                binding.btnConfirm.text = getString(R.string.btn_report)
            }
            "신고_댓글" -> {
                binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_report) })
                binding.tvDialogMessage.text = getString(R.string.dialog_report, "댓글")
                binding.btnConfirm.text = getString(R.string.btn_report)
            }
            "삭제_감상평" -> {
                binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_delete) })
                binding.tvDialogMessage.text = getString(R.string.dialog_delete, "감상평")
                binding.btnConfirm.text = getString(R.string.btn_delete_dialog)
            }
            "삭제_댓글" -> {
                binding.ivDialogIllus.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.illus_dialog_delete) })
                binding.tvDialogMessage.text = getString(R.string.dialog_delete, "댓글")
                binding.btnConfirm.text = getString(R.string.btn_delete_dialog)
            }
            "감상평" -> {
                Log.d(TAG, "onCreateView: 감상평")
                binding.ivDialogIllus.isVisible = false
                binding.tvDialogMessage.text = getString(R.string.dialog_close_review)
                binding.btnConfirm.text = getString(R.string.btn_wrting_review_close)
                binding.btnConfirm.setOnClickListener {
                    Log.d(TAG, "onCreateView: btn clicked")
                    dismiss()
                    Navigation.findNavController(it).popBackStack()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        //확인 버튼 클릭 시
        binding.btnConfirm.setOnClickListener {
            when(type){
                "신고_감상평" -> {

                }
                "신고_댓글" -> {

                }
                "삭제_감상평" -> {

                }
                "삭제_댓글" -> {

                }
                "감상평" -> {
                    parentFragment?.view?.let { it1 -> Navigation.findNavController(it1).popBackStack() }
                }
            }
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
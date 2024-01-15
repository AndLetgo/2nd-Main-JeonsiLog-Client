package com.example.jeonsilog.view.admin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogAdminBinding
import com.example.jeonsilog.viewmodel.AdminViewModel

class DialogAdmin(private val type:String, private val defaultText: String) : DialogFragment() {
    private var _binding: DialogAdminBinding? = null
    private val binding get() = _binding!!
    private val adminViewModel: AdminViewModel by activityViewModels()
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_admin, container, false)

        binding.etDialogAdmin.setText(defaultText, TextView.BufferType.EDITABLE)
        when(type){
            "exhibitionName" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_exhibition_name)}
            "placeName" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_place_name)}
            "placeAddress" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_place_address)}
            "placeCall" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_place_call)}
            "placeHomepage" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_place_homepage)}
            "exhibitionInformation" -> {binding.tvTitle.text = getString(R.string.dialog_admin_edit_exhibition_information)}
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {
            val editTextText = binding.etDialogAdmin.text.toString()
            when(type){
                "exhibitionName" -> {adminViewModel.setExhibitionName(editTextText)}
                "placeName" -> {adminViewModel.setPlaceName(editTextText)}
                "placeAddress" -> {adminViewModel.setAddress(editTextText)}
                "placeCall" -> {adminViewModel.setPlaceCall(editTextText)}
                "placeHomepage" -> {adminViewModel.setPlaceHomepage(editTextText)}
                "exhibitionInformation" -> {adminViewModel.setExhibitionInformation(editTextText)}
            }
            adminViewModel.setIsChanged(true)
            dismiss()
        }

        return binding.root
    }
}
package com.example.jeonsilog.view.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogAdminBinding

class DialogAdmin : DialogFragment() {
    private var _binding: DialogAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_admin, container, false)
    }

}
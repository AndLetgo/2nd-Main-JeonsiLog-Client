package com.example.jeonsilog.view.mypage


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.DialogReconfirmUnlinkBinding
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageUnLinkDialog(): DialogFragment() {
    private var _binding: DialogReconfirmUnlinkBinding? = null
    private val binding get() = _binding!!

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_reconfirm_unlink, container, false)
//        binding.lifecycleOwner = this

        binding.btnDialogReconfirmUnlinkCancel.setOnClickListener {
            dismiss()
        }

        binding.btnDialogReconfirmDoUnlink.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if(error != null){
                    Log.e("LOGIN", error.message.toString())
                }
                CoroutineScope(Dispatchers.IO).launch{
                    if(UserRepositoryImpl().doUnLink(encryptedPrefs.getAT().toString())){
                        launch(Dispatchers.Main) {
                            val intent = Intent(requireContext(), SplashActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
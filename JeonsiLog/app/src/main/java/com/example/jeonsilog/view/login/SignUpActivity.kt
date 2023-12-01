package com.example.jeonsilog.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ActivitySignupBinding
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.ImageUploader
import com.example.jeonsilog.widget.utils.NickValidChecker
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val TAG = this.javaClass.simpleName
    private lateinit var profileUri: Uri

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 버튼 이벤트 처리
            Log.e(TAG, "뒤로가기 클릭")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.hide()

        this.onBackPressedDispatcher.addCallback(this, callback)

        UserApiClient.instance.me { user, error ->
            if(error != null){
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                profileUri = user.kakaoAccount?.profile?.profileImageUrl!!.toUri()

                GlobalScope.launch(Dispatchers.IO) {
                    ImageUploader().uploadImageToServer(profileUri, applicationContext)
                }

                loadProfileImage(profileUri)
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.vm = viewModel

        binding.lifecycleOwner = this
        viewModel.setComment(getString(R.string.login_nick_hint))

        val checker = NickValidChecker()

        binding.etNick.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setComment(getString(R.string.login_nick_hint))
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val inputText = p0.toString()

                if (checker.allCheck(inputText)) {
                    //if(닉네임 중복 체크){
                    viewModel.setComment(getString(R.string.login_nick_check_success))
                    activeTransition(true)
//                  }
//                  else {
//                    viewModel.setComment(getString(R.string.login_nick_check_duplicate))
//                  }
                }
                else {
                    if (!checker.isLengthValid(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_hint))
                    }
                    else if (checker.hasSpecialCharacter(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_check_special_char))
                    }
                    else if (checker.hasProhibitedWord(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_check_prohibited_words))
                    }
                    else if(checker.isNotPair(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_check_is_pair))
                    }
                    activeTransition(false)
                }
            }
        })

        binding.btnUpdateProfileUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding.btnLogin.setOnClickListener {

        }
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagePath = result.data!!.data

            if (imagePath != null) {
                try {
                    loadProfileImage(imagePath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                // 동기적 처리 필요
                GlobalScope.launch(Dispatchers.IO) {
                    ImageUploader().uploadImageToServer(imagePath, applicationContext)
                }
            }
        }
    }

    private fun loadProfileImage(url: Uri?){
        GlideApp
            .with(this)
            .load(url)
            .optionalCircleCrop()
            .into(binding.ivLoginProfile)
    }

    private fun activeTransition(p0: Boolean){
        if(p0){
            binding.btnLogin.background = getDrawable(R.drawable.shape_corner_round_start_btn_activate)
            binding.btnLogin.setTextColor(getColor(R.color.basic_white))
        }
        else {
            binding.btnLogin.background = getDrawable(R.drawable.shape_corner_round_start_btn)
            binding.btnLogin.setTextColor(getColor(R.color.gray_medium))
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
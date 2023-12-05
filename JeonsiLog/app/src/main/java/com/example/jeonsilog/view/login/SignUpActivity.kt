package com.example.jeonsilog.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.auth.SignUpData
import com.example.jeonsilog.databinding.ActivitySignupBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.example.jeonsilog.widget.utils.NickValidChecker
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val tag = this.javaClass.simpleName
    private var backPressedTime: Long = 0L


    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(applicationContext, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.hide()

        if(prefs.getSignUpFinished()){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        this.onBackPressedDispatcher.addCallback(this, callback)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.vm = viewModel

        binding.lifecycleOwner = this
        viewModel.setComment(getString(R.string.login_nick_hint))
        prefs.setSignUpFinished(false)

        UserApiClient.instance.me { user, error ->
            if(error != null){
                Log.e(tag, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(tag, "사용자 정보 요청 성공 : $user")
                val profileUri = user.kakaoAccount?.profile?.profileImageUrl!!.toString()
                viewModel.setProfileUrl(profileUri)
            }
        }

        binding.etNick.setOnFocusChangeListener{ v: View? , hasFocus: Boolean ->
            viewModel.onNickFocusChange(v, hasFocus)
        }

        val checker = NickValidChecker()

        binding.etNick.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setComment(getString(R.string.login_nick_hint))
                viewModel.onBtnFlagChange(true)
                viewModel.onCheckableFlagChange(false)
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val inputText = p0.toString()

                if (checker.allCheck(inputText)) {
                    viewModel.setComment(getString(R.string.login_nick_check_success))
                    viewModel.onCheckableFlagChange(true)
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
                    viewModel.onCheckableFlagChange(false)
                }
            }
        })

        binding.btnUpdateProfileUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding.btnLoginDuplicate.setOnClickListener {
            viewModel.duplicateCheck(binding.etNick.text.toString(), getString(R.string.login_nick_check_duplicate))
        }

        binding.btnLoginStart.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch{
                val data = SignUpData("","","","")

                UserApiClient.instance.me { user, error ->
                    if(error != null){
                        Log.e(tag, error.message.toString())
                    } else {
                        if(user != null){
                            data.providerId = user.id.toString()
                            data.nickname = user.kakaoAccount!!.profile!!.nickname.toString()
                            data.email = user.kakaoAccount!!.email.toString()
                            data.profileImgUrl = user.kakaoAccount!!.profile!!.profileImageUrl.toString()
                        }
                    }
                }

                AuthRepositoryImpl().postUser(data)

            }

            prefs.setSignUpFinished(true)

            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        binding.ibEditClear.setOnClickListener {
            binding.etNick.text = null
        }

        viewModel.profileImagePath.observe(this) { path ->
            path?.let {
                loadProfileImage(it)
            }
        }
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagePath = result.data!!.data

            if (imagePath != null) {
                try {
                    viewModel.setProfileUrl(imagePath.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadProfileImage(path: String){
        GlideApp
            .with(this)
            .load(path)
            .optionalCircleCrop()
            .into(binding.ivLoginProfile)
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
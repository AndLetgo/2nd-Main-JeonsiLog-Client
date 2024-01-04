package com.example.jeonsilog.view.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest
import com.example.jeonsilog.databinding.ActivitySignupBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.testDefalutImg
import com.example.jeonsilog.widget.utils.ImageUtil
import com.example.jeonsilog.widget.utils.NickValidChecker
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val tag = this.javaClass.simpleName
    private var backPressedTime: Long = 0L
    private var uploadImg: Uri? = null


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
                viewModel.setProfileUrl(testDefalutImg)
            }
        }

        binding.etNick.setOnFocusChangeListener{ v: View? , hasFocus: Boolean ->
            viewModel.onNickFocusChange(v, hasFocus)
        }

        val checker = NickValidChecker()

        binding.etNick.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setComment(getString(R.string.login_nick_hint))
                viewModel.onBtnFlagChange(false)
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
                    // API 제작 대기중
//                    else if (){
//                        viewModel.setComment(getString(R.string.login_nick_check_prohibited_words))
//                    }
                    else if(checker.isNotPair(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_check_is_pair))
                    }
                    viewModel.onCheckableFlagChange(false)
                }
            }
        })

        binding.btnUpdateProfileUrl.setOnClickListener {
            if(checkPermissions(this)){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            } else {
                Toast.makeText(this, "갤러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginDuplicate.setOnClickListener {
            if(viewModel.checkableFlag.value == true){
                viewModel.duplicateCheck(binding.etNick.text.toString(), getString(R.string.login_nick_check_duplicate))
            }
        }

        binding.btnLoginStart.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                try {
                    val userData = getUserData()
                    Log.d(tag, userData.toString())

                    if (userData != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (AuthRepositoryImpl().postSignUp(userData)) {
                                val data = getUserDataFromKakao()
                                if(AuthRepositoryImpl().signIn(data!!)){
                                    patchMyProfileImg()

                                    CoroutineScope(Dispatchers.Main).launch {
                                        val intent = Intent(this@SignUpActivity, SplashActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    Log.d(tag, "로그인 에러")
                                }
                            } else {
                                Log.d(tag, "회원가입 에러")
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
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

    private suspend fun getUserData(): SignUpRequest? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(tag, error.message.toString())
                    continuation.resume(null)
                } else {
                    if (user != null) {
                        val data = SignUpRequest(
                            providerId = user.id.toString(),
                            nickname = viewModel.etNick.value!!,
                            email = user.kakaoAccount!!.email.toString(),
                            profileImgUrl = testDefalutImg
                        )
                        continuation.resume(data)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        }
    }

    private suspend fun getUserDataFromKakao(): SignInRequest? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(tag, error.message.toString())
                    continuation.resume(null)
                } else {
                    if (user != null) {
                        val data = SignInRequest(
                            providerId = user.id.toString(),
                            email = user.kakaoAccount!!.email.toString(),
                        )
                        continuation.resume(data)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        }
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            uploadImg = result.data!!.data!!

            if (uploadImg != null) {
                try {
                    viewModel.setProfileUrl(uploadImg.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun patchMyProfileImg() {
        if(uploadImg != null){
            val file = File(ImageUtil().absolutelyPath(this, uploadImg!!))
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)

            CoroutineScope(Dispatchers.IO).launch{
                val response = UserRepositoryImpl().uploadProfileImg(GlobalApplication.encryptedPrefs.getAT(), filePart)
                Log.d(tag, filePart.body.toString())
                if(response.isSuccessful && response.body()!!.check){
                    Log.d("Upload", "Image uploaded successfully")
                } else {
                    Log.e("Upload", "Image upload failed")
                }
            }
        }
        // null이면 기본 이미지 저장
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

    private fun checkPermissions(context: Context): Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
            }
        } else {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
        }

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }
}
package com.example.jeonsilog.view.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest
import com.example.jeonsilog.databinding.FragmentSignupBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
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

class SignUpFragment: BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {
    private val viewModel: SignUpViewModel by activityViewModels()
    private var uploadImg: Uri? = null

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.setComment(getString(R.string.login_nick_hint))
        GlobalApplication.prefs.setSignUpFinished(false)

        UserApiClient.instance.me { user, error ->
            if(error != null){
                Log.e(tag, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(tag, "사용자 정보 요청 성공 : $user")
            }
        }

        binding.etNick.setOnFocusChangeListener{ v: View?, hasFocus: Boolean ->
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
                    else if(checker.isNotPair(inputText)){
                        viewModel.setComment(getString(R.string.login_nick_check_is_pair))
                    }
                    viewModel.onCheckableFlagChange(false)
                }
            }
        })

        binding.btnUpdateProfileUrl.setOnClickListener {
            if((requireContext() as SignUpActivity).checkPermission()){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            } else {
                Toast.makeText(requireContext(), "갤러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginDuplicate.setOnClickListener {
            if(viewModel.checkableFlag.value == true){
                viewModel.nickAvailableCheck(binding.etNick.text.toString(), requireContext())
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
                                        val intent = Intent(requireContext(), SplashActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                        startActivity(intent)
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
                            nickname = binding.etNick.text.toString(),
                            email = user.kakaoAccount!!.email.toString(),
                            profileImgUrl = null
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
            val file = File(ImageUtil().absolutelyPath(requireContext(), uploadImg!!))
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
}
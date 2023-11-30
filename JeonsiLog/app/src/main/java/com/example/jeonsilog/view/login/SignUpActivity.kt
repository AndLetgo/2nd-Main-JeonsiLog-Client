package com.example.jeonsilog.view.login

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.jeonsilog.R
import com.example.jeonsilog.databinding.ActivitySignupBinding
import com.example.jeonsilog.viewmodel.SignUpViewModel
import com.example.jeonsilog.widget.utils.NickValidChecker

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.hide()

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

                if (checker.isLengthValid(inputText) && !checker.hasSpecialCharacter(inputText) && !checker.hasProhibitedWord(inputText)) {
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
                    activeTransition(false)
                }
            }
        })
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
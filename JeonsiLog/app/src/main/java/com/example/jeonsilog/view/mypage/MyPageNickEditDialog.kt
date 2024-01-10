package com.example.jeonsilog.view.mypage


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.user.EditNickRequest
import com.example.jeonsilog.databinding.DialogNicknameEditBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.viewmodel.MyPageNickEditDialogViewModel
import com.example.jeonsilog.viewmodel.MyPageViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.NickValidChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageNickEditDialog(private val parentVm: MyPageViewModel): DialogFragment() {
    private val viewModel: MyPageNickEditDialogViewModel by viewModels()
    private var _binding: DialogNicknameEditBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 324

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_nickname_edit, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.setComment(getString(R.string.dialog_nick_modify_hint))

        binding.etDialogNickEdit.setOnFocusChangeListener{ v: View? , hasFocus: Boolean ->
            viewModel.onNickFocusChange(v, hasFocus)
        }
        val checker = NickValidChecker()

        binding.etDialogNickEdit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setComment(getString(R.string.login_nick_hint))
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val inputText = p0.toString()

                if (checker.allCheck(inputText)) {
                    viewModel.setComment(getString(R.string.login_nick_check_success))
                    viewModel.setFlag(true)
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
                    viewModel.setFlag(false)
                }
            }
        })

        binding.ibDialogNickEditClear.setOnClickListener {
            binding.etDialogNickEdit.text = null
        }

        binding.apply {
            btnDialogNickEditCancel.setOnClickListener{
                dismiss()
            }
            btnDialogNickEditModify.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = AuthRepositoryImpl().getIsAvailable(binding.etDialogNickEdit.text.toString())
                    if(response.isSuccessful && response.body()!!.check){
                        val result = response.body()!!.information
                        if(!result.isDuplicate && !result.isForbidden){
                            val flag = UserRepositoryImpl().patchNick(encryptedPrefs.getAT(), EditNickRequest(binding.etDialogNickEdit.text.toString()))

                            launch(Dispatchers.Main) {
                                if(flag){
                                    parentVm.setNick(encryptedPrefs.getNN()!!)
                                    dismiss()
                                }
                            }
                        } else if (result.isDuplicate){
                            launch(Dispatchers.Main){
                                viewModel.setComment(getString(R.string.login_nick_check_duplicate))
                            }
                        } else {
                            launch(Dispatchers.Main){
                                viewModel.setComment(getString(R.string.login_nick_check_forbidden))
                            }
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
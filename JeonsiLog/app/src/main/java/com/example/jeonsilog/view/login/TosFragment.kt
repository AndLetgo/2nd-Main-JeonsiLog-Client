package com.example.jeonsilog.view.login

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentTosBinding
import com.example.jeonsilog.viewmodel.SignUpViewModel
import kotlinx.coroutines.runBlocking

class TosFragment: BaseFragment<FragmentTosBinding>(R.layout.fragment_tos) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.changeTosPhoto((requireContext() as SignUpActivity).checkPermission())

        binding.cbTosAll.setOnClickListener {
            val temp = viewModel.tosIsCheckedAll.value!!
            runBlocking {
                (requireContext() as SignUpActivity).requestPermission()
            }

            viewModel.changeAll(viewModel.tosIsCheckedPermissionPhoto.value!!)
            viewModel.changeTosTos(!temp)

        }

        binding.cbTosTos.setOnClickListener {
            viewModel.changeTosTos(!viewModel.tosIsCheckedTos.value!!)
        }

        binding.cbTosPermissionPhoto.setOnClickListener {
            (requireContext() as SignUpActivity).requestPermission()
        }

        binding.btnTosNext.setOnClickListener {
            if(viewModel.tosIsCheckedTos.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, SignUpFragment())
                    .commit()
            }
        }

        binding.ibTosWeb.setOnClickListener {
            val webpage = Uri.parse("https://sites.google.com/view/jeonsilog/%ED%99%88")
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }
        }
    }
}
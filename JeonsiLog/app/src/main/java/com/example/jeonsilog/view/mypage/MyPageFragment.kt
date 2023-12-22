package com.example.jeonsilog.view.mypage

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.BottomSheetMypageProfileEditBinding
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.MyPageViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.io.IOException

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    private var _bsBinding: BottomSheetMypageProfileEditBinding? = null
    private val bsBinding get() = _bsBinding

    override fun init() {
        viewModel.getMyInfo()
        binding.vm = viewModel
        binding.lifecycleOwner = requireActivity()

        val mainActivity = activity as MainActivity
        mainActivity.setStateBn(true)

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        _bsBinding = BottomSheetMypageProfileEditBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bsBinding!!.root)

        viewModel.profileImg.observe(this){
            it?.let {
                loadProfileImage(it)
            }
        }

        val tabTextList = listOf(getString(R.string.mypage_my_rating), getString(R.string.mypage_my_review), getString(R.string.mypage_favorites))

        binding.vpMypage.adapter = MyPageVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlMypage, binding.vpMypage){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.ibMypageNickEdit.setOnClickListener {
            Log.d("TAG", "editNick")
            showCustomDialog()
        }

        binding.ibMypageProfileEdit.setOnClickListener {
            Log.d("TAG", "editProfile")
            bottomSheetDialog.show()
        }

        bsBinding!!.btnBottomSheetMypageLoadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
            bottomSheetDialog.dismiss()
        }

        bsBinding!!.btnBottomSheetMypageLoadDefalut.setOnClickListener {
            viewModel.setProfileImg("data:image/jpeg;base64,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")
            bottomSheetDialog.dismiss()
        }

        binding.ibMypageSetting.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, MyPageSettingFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.tvMypageFollow.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, MyPageListFragment(0))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.tvMypageFollowing.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, MyPageListFragment(1))
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    private fun showCustomDialog() {
        val customDialogFragment = MyPageNickEditDialog(viewModel)
        customDialogFragment.show(parentFragmentManager, "MyPageNickEditDialog")
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagePath = result.data!!.data

            if (imagePath != null) {
                try {
                    viewModel.setProfileImg(imagePath.toString())
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
            .into(binding.ivMypageProfile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bsBinding = null
    }
}
package com.example.jeonsilog.view.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.BottomSheetMypageProfileEditBinding
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.MyPageViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.testDefalutImg
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
            val mActivity = activity as MainActivity
            if(mActivity.checkPermissions(requireContext())){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "갤러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        bsBinding!!.btnBottomSheetMypageLoadDefalut.setOnClickListener {
            viewModel.setProfileImg(testDefalutImg)
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
            val imageUri = result.data!!.data

            if (imageUri != null) {
                try {
                    viewModel.setProfileImg(imageUri.toString())
                    patchMyProfileImg(imageUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun patchMyProfileImg(uri: Uri) {
        val file = File(ImageUtil().absolutelyPath(requireContext(), uri))
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)

        CoroutineScope(Dispatchers.IO).launch{
            val response = UserRepositoryImpl().uploadProfileImg(encryptedPrefs.getAT(), filePart)
            Log.d(tag, filePart.body.toString())
            if(response.isSuccessful && response.body()!!.check){
                Log.d("Upload", "Image uploaded successfully")
            } else {
                Log.e("Upload", "Image upload failed")
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
package com.example.jeonsilog.view.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    private var _bsBinding: BottomSheetMypageProfileEditBinding? = null
    private val bsBinding get() = _bsBinding
    private lateinit var nowActivityName :String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        nowActivityName=context.javaClass.simpleName
    }
    override fun init() {
        try{
            (activity as MainActivity).setStateBn(true)
        }catch (e:ClassCastException){
            // ExtraActivity는 처리 필요 없음
        }
        if(nowActivityName == "MainActivity"){
            (requireActivity()  as MainActivity).setBottomNavCurrentItem(4)
        }
        viewModel.getMyInfo()
        binding.vm = viewModel
        binding.lifecycleOwner = requireActivity()


        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(MyPageFragment())
                isRefresh.value = false
            }
        }

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        _bsBinding = BottomSheetMypageProfileEditBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bsBinding!!.root)

        viewModel.profileImg.observe(this){
            it?.let {
                loadProfileImage(it, null)
            }
        }

        val tabTextList = listOf(getString(R.string.mypage_my_rating), getString(R.string.mypage_my_review), getString(R.string.mypage_favorites))

        binding.vpMypage.adapter = MyPageVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlMypage, binding.vpMypage){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.ibMypageNickEdit.setOnClickListener {
            showCustomDialog()
        }

        binding.ibMypageProfileEdit.setOnClickListener {
            bottomSheetDialog.show()
        }

        bsBinding!!.btnBottomSheetMypageLoadImage.setOnClickListener {
            val mActivity = activity as MainActivity
            if(mActivity.checkPermission()){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
                bottomSheetDialog.dismiss()
            } else {
                mActivity.requestPermission()
            }
        }

        bsBinding!!.btnBottomSheetMypageLoadDefalut.setOnClickListener {
            setDefalutImage()
            loadProfileImage(null, R.drawable.illus_default_profile)
            bottomSheetDialog.dismiss()
        }

        binding.ibMypageSetting.setOnClickListener {
            if(nowActivityName == "MainActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fl_main, MyPageSettingFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            if(nowActivityName == "ExtraActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fcv_nav_frame, MyPageSettingFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        binding.tvMypageFollow.setOnClickListener {
            if(nowActivityName == "MainActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fl_main, MyPageListFragment(0))

                transaction.addToBackStack(null)
                transaction.commit()
            }
            if(nowActivityName == "ExtraActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fcv_nav_frame, MyPageListFragment(0))

                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        binding.tvMypageFollowing.setOnClickListener {
            if(nowActivityName == "MainActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fl_main, MyPageListFragment(1))

                transaction.addToBackStack(null)
                transaction.commit()
            }
            if(nowActivityName == "ExtraActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fcv_nav_frame, MyPageListFragment(1))

                transaction.addToBackStack(null)
                transaction.commit()
            }
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

            if(response.isSuccessful && response.body()!!.check){
                Log.d("Upload", "Image uploaded successfully")
            } else {
                Log.e("Upload", "Image upload failed")
            }
        }
    }

    private fun setDefalutImage() {
        val outputDir: File = requireActivity().cacheDir
        val outputFile: File = File.createTempFile("prefix", "suffix", outputDir)

        val requestFile: RequestBody = outputFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("image", null, requestFile)

        outputFile.deleteOnExit()

        CoroutineScope(Dispatchers.IO).launch{
            val response = UserRepositoryImpl().uploadProfileImg(encryptedPrefs.getAT(), imagePart)

            if(response.isSuccessful && response.body()!!.check){
                Log.d("Upload", "Image uploaded successfully")
                encryptedPrefs.setURL(null)
            } else {
                Log.e("Upload", "Image upload failed")
            }
        }

//        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.illus_default_profile)
//        val bitmap = (drawable as BitmapDrawable).bitmap
//
//        val file = File(requireContext().cacheDir, "default.png")
//        val outputStream: OutputStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        outputStream.flush()
//        outputStream.close()
//
//        val uri = Uri.fromFile(file)
//
//        patchMyProfileImg(uri)
    }


    private fun loadProfileImage(path: String?, id: Int?){

        if(path.isNullOrEmpty()){
            GlideApp
                .with(this)
                .load(id)
                .optionalCircleCrop()
                .into(binding.ivMypageProfile)
        } else {
            GlideApp
                .with(this)
                .load(path)
                .optionalCircleCrop()
                .into(binding.ivMypageProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bsBinding = null
    }
}
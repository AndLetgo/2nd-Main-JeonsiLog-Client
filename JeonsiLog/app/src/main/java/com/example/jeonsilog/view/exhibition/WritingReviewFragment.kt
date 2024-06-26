package com.example.jeonsilog.view.exhibition

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.PatchReviewRequest
import com.example.jeonsilog.data.remote.dto.review.PostReviewRequest
import com.example.jeonsilog.databinding.FragmentWritingReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.viewmodel.ExhibitionWritingViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class WritingReviewFragment : BaseFragment<FragmentWritingReviewBinding>(
    R.layout.fragment_writing_review), DialogWithIllusInterface {
    private lateinit var dialogWithIllus: DialogWithIllus
    private val viewModel: ExhibitionWritingViewModel by viewModels()
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private var thisExhibitionId = 0
    val TAG = "writing"
    override fun init() {
        thisExhibitionId = exhibitionViewModel.currentExhibitionIds.value!![exhibitionViewModel.currentExhibitionIds.value!!.size-1]

        if(exhibitionViewModel.checkReviewEntity.value!!.isWrite){
            binding.etWritingReview.setText(
                exhibitionViewModel.checkReviewEntity.value!!.contents,
                TextView.BufferType.EDITABLE
            )
            viewModel.setWritingCount(exhibitionViewModel.checkReviewEntity.value!!.contents.length.toString())
            if(viewModel.writingCount.value!!.toInt() > 0){
                viewModel.setCheckCount(true)
            }
        }

        binding.btnCancel.setOnClickListener {
            showCustomDialog( "감상평", -1, -1)
        }

        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.etWritingReview.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setWritingCount(s?.length.toString())

                if(s?.length!! > 0 ){
                    viewModel.setCheckCount(true)
                }else{
                    viewModel.setCheckCount(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.btnConfirm.setOnClickListener {
            if(viewModel.checkCount.value!!){
                var isSuccess = false
                if(exhibitionViewModel.checkReviewEntity.value!!.isWrite){
                    runBlocking(Dispatchers.IO){
                        val body = PatchReviewRequest(
                            exhibitionViewModel.checkReviewEntity.value!!.reviewId,
                            binding.etWritingReview.text.toString()
                        )
                        val response = ReviewRepositoryImpl().patchReview(encryptedPrefs.getAT(),body)
                        if(response.isSuccessful && response.body()!!.check){
                            isSuccess = true
                        }
                    }
                }else{
                    runBlocking(Dispatchers.IO) {
                        val body = PostReviewRequest(thisExhibitionId, binding.etWritingReview.text.toString())
                        val response = ReviewRepositoryImpl().postReview(encryptedPrefs.getAT(), body)
                        if(response.isSuccessful && response.body()!!.check){
                            isSuccess = true
                        }else{
                            null
                        }
                    }
                }
                if(isSuccess){
                    Log.d(TAG, "init: 성공")
                    checkLevelUp()
                    encryptedPrefs.setReviewCount(encryptedPrefs.getReviewCount()+1)
                    exhibitionViewModel.setUserReview(binding.etWritingReview.text.toString())

                    exhibitionViewModel.resetCheckReviewEntity()
                    Navigation.findNavController(it).popBackStack()
                }
            }
        }

        viewModel.writingCount.observe(this){
            binding.tvCountWritingReview.text =
                getString(R.string.exhibition_writing_review_count, it)
        }
    }

    private fun showCustomDialog(type:String, contentId:Int, position:Int) {
        val customDialogFragment = DialogWithIllus(type, contentId, 0, position, this)
        customDialogFragment.show(parentFragmentManager, tag)
    }

    override fun confirmButtonClick(position: Int) {}

    private fun showLevelUpDialog(userLevel:String){
        val dialog = DialogLevelUp(requireContext(), userLevel)
        dialog.show()
    }
    private fun checkLevelUp(){
        Log.d(TAG, "checkLevelUp: ${encryptedPrefs.getReviewCount()}")
        if(encryptedPrefs.getUserLevel()=="NON"){
            showLevelUpDialog("NON")
        }else{
            when(encryptedPrefs.getReviewCount()){
                2 -> showLevelUpDialog(getString(R.string.user_level_beginner))
                9 -> showLevelUpDialog(getString(R.string.user_level_intermediate))
                19 -> showLevelUpDialog(getString(R.string.user_level_advanced))
                29 -> showLevelUpDialog(getString(R.string.user_level_master))
            }
        }
    }
}
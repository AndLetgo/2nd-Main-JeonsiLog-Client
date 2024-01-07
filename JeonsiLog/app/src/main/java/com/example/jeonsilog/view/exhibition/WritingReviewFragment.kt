package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.PostReviewRequest
import com.example.jeonsilog.databinding.FragmentWritingReviewBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionWritingViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class WritingReviewFragment : BaseFragment<FragmentWritingReviewBinding>(
    R.layout.fragment_writing_review) {
    private lateinit var dialogWithIllus: DialogWithIllus
    private val viewModel: ExhibitionWritingViewModel by viewModels()
    private var thisExhibitionId = 0
    val TAG = "writing"
    override fun init() {
        thisExhibitionId = requireArguments().getInt("exhibitionId")
        
        binding.btnCancel.setOnClickListener {
            ExtraActivity().showCustomDialog(parentFragmentManager, "감상평", -1, -1, -1)
        }

        binding.vm = viewModel
        binding.etWritingReview.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("writing", "onTextChanged: s?.length: ${s?.length}")
                viewModel.setWritingCount(s?.length.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.btnConfirm.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                val body = PostReviewRequest(thisExhibitionId, binding.etWritingReview.text.toString())
                val response = ReviewRepositoryImpl().postReview(encryptedPrefs.getAT(), body)
                if(response.isSuccessful && response.body()!!.check){
                    Log.d(TAG, "init: post successful")
                }else{
                    null
                }
            }
            Navigation.findNavController(it).popBackStack()
        }

        viewModel.writingCount.observe(this){
            Log.d(TAG, "init: viewModel.writingCount: $it")
            binding.tvCountWritingReview.text =
                getString(R.string.exhibition_writing_review_count, it)
        }
    }
}
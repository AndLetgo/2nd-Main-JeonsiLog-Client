package com.example.jeonsilog.view.otheruser

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserBinding
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.google.android.material.tabs.TabLayoutMediator

class OtherUserFragment: BaseFragment<FragmentOtherUserBinding>(R.layout.fragment_other_user) {
    private val viewModel: OtherUserViewModel by viewModels()

    override fun init() {
        binding.vm = viewModel
        testViewModel()
        loadImage()

        binding.btnOtherUserFollowing.setOnClickListener {
            viewModel.changeFlag()

            // 서버에 팔로우 여부
        }

        viewModel.flag.observe(this) {
            if(it){
                with(binding){
                    btnOtherUserFollowing.text = getString(R.string.btn_following)
                    btnOtherUserFollowing.background = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_corner_round_follower_btn)
                    btnOtherUserFollowing.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.gray_medium))
                }
            } else {
                with(binding){
                    btnOtherUserFollowing.text = getString(R.string.btn_follow)
                    btnOtherUserFollowing.background = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_corner_round_follower_btn_activate)
                    btnOtherUserFollowing.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.basic_white))
                }
            }
        }

        val tabTextList = listOf(getString(R.string.other_rating), getString(R.string.other_review), getString(R.string.other_photo))

        binding.vpOtherUser.adapter = OtherUserVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlOtherUser, binding.vpOtherUser){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

    private fun testViewModel(){
        viewModel.testSet()
        viewModel.setTitle(getString(R.string.other_nick_title, viewModel.nick.value))
    }

    private fun loadImage(){
        GlideApp.with(this)
            .load(viewModel.profileImg.value)
            .optionalCircleCrop()
            .into(binding.ivOtherUserProfile)
    }
}
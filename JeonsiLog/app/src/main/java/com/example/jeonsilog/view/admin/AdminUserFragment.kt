package com.example.jeonsilog.view.admin

import android.view.View
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminUserBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.otheruser.OtherUserVpAdapter
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.google.android.material.tabs.TabLayoutMediator

class AdminUserFragment(private val otherUserId: Int, private val otherUserNick: String): BaseFragment<FragmentAdminUserBinding>(R.layout.fragment_admin_user) {
    private val viewModel: OtherUserViewModel by viewModels()

    override fun init() {
        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(AdminUserFragment(otherUserId, otherUserNick))
                GlobalApplication.isRefresh.value = false
            }
        }

        viewModel.getOtherUserInfo(otherUserId)

        viewModel.following.observe(this){
            binding.vm = viewModel
            viewModel.setTitle(requireContext())
            loadImage()
        }

        binding.btnStop.setOnClickListener {

        }

        val tabTextList = listOf(getString(R.string.other_rating), getString(R.string.other_review), getString(R.string.other_photo))

        binding.vpOtherUser.adapter = OtherUserVpAdapter(this.requireActivity(), viewModel, otherUserId)

        TabLayoutMediator(binding.tlOtherUser, binding.vpOtherUser){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        viewModel.level.observe(this) {
            when (it) {
                "BEGINNER" -> { // 3~9
                    binding.ivLevel.visibility = View.VISIBLE
                    binding.ivLevel.setImageResource(R.drawable.ic_user_level_1_beginner)
                }

                "INTERMEDIATE" -> { // 10~19
                    binding.ivLevel.visibility = View.VISIBLE
                    binding.ivLevel.setImageResource(R.drawable.ic_user_level_2_intermediate)
                }

                "ADVANCED" -> {
                    binding.ivLevel.visibility = View.VISIBLE
                    binding.ivLevel.setImageResource(R.drawable.ic_user_level_3_expert)
                }

                "MASTER" -> {
                    binding.ivLevel.visibility = View.VISIBLE
                    binding.ivLevel.setImageResource(R.drawable.ic_user_level_4_master)
                }

                else -> {
                    binding.ivLevel.visibility = View.GONE
                }
            }
        }
    }

    private fun loadImage(){
        GlideApp.with(this)
            .load(viewModel.profileImg.value)
            .optionalCircleCrop()
            .into(binding.ivOtherUserProfile)
    }
}
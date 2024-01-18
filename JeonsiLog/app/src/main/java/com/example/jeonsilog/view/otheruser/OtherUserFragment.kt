package com.example.jeonsilog.view.otheruser

import android.content.Context
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.google.android.material.tabs.TabLayoutMediator

class OtherUserFragment(private val otherUserId: Int, private val otherUserNick: String): BaseFragment<FragmentOtherUserBinding>(R.layout.fragment_other_user) {

    private val viewModel: OtherUserViewModel by viewModels()
    private lateinit var nowActivityName :String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        nowActivityName=context.javaClass.simpleName
    }

    override fun init() {
        try{
            (activity as MainActivity).setStateBn(false, "user")
        }catch (e:ClassCastException){

        }


        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(OtherUserFragment(otherUserId, otherUserNick))
                GlobalApplication.isRefresh.value = false
            }
        }

        viewModel.getOtherUserInfo(otherUserId)

        viewModel.following.observe(this){
            binding.vm = viewModel
            viewModel.setTitle(requireContext())
            loadImage()
        }

        binding.btnOtherUserFollowing.setOnClickListener {
            viewModel.changeFlag()
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

        binding.vpOtherUser.adapter = OtherUserVpAdapter(this.requireActivity(), viewModel, otherUserId)

        TabLayoutMediator(binding.tlOtherUser, binding.vpOtherUser){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.tvOtherUserFollow.setOnClickListener {
            Log.d("TAG", "$nowActivityName: ")
            if(nowActivityName == "MainActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fl_main, OtherUserListFragment(0, otherUserId, otherUserNick))

                transaction.addToBackStack(null)
                transaction.commit()
            }
            if(nowActivityName == "ExtraActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fcv_nav_frame, OtherUserListFragment(0, otherUserId, otherUserNick))

                transaction.addToBackStack(null)
                transaction.commit()
            }


        }

        binding.tvOtherUserFollowing.setOnClickListener {
            if(nowActivityName == "MainActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fl_main, OtherUserListFragment(1, otherUserId, otherUserNick))

                transaction.addToBackStack(null)
                transaction.commit()
            }
            if(nowActivityName == "ExtraActivity"){
                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fcv_nav_frame, OtherUserListFragment(1, otherUserId, otherUserNick))

                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
    }

    private fun loadImage(){
        //(context as MainActivity).
        GlideApp.with(this)
            .load(viewModel.profileImg.value)
            .optionalCircleCrop()
            .into(binding.ivOtherUserProfile)
    }
}
package com.example.jeonsilog.view

import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.view.otheruser.OtherUserFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun init() {
        binding.btnUnlink.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, OtherUserFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
package com.example.jeonsilog.view.admin

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminReviewBinding
import com.example.jeonsilog.view.exhibition.AdminReviewReplyRvAdapter

class AdminReviewFragment : BaseFragment<FragmentAdminReviewBinding>(R.layout.fragment_admin_review) {
    private lateinit var adminReviewReplyRvAdapter: AdminReviewReplyRvAdapter
    override fun init() {
        //RecyclerView
        val list = listOf<ReplyModel>(
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content"),
            ReplyModel("","name","date","content")
        )
        adminReviewReplyRvAdapter = AdminReviewReplyRvAdapter(list)
        binding.rvExhibitionReviewReply.adapter = adminReviewReplyRvAdapter
        binding.rvExhibitionReviewReply.layoutManager = LinearLayoutManager(this.context)


    }
}
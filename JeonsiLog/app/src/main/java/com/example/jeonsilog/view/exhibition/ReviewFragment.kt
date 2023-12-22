package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentReviewBinding

class ReviewFragment : BaseFragment<FragmentReviewBinding>(R.layout.fragment_review) {
    private lateinit var exhibitionReplyRvAdapter: ExhibitionReplyRvAdapter
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
        exhibitionReplyRvAdapter = ExhibitionReplyRvAdapter(list)
        binding.rvExhibitionReviewReply.adapter = exhibitionReplyRvAdapter
        binding.rvExhibitionReviewReply.layoutManager = LinearLayoutManager(this.context)

        binding.ibMenu.setOnClickListener {
            (activity as ExtraActivity).setMenuButton(it)
        }

        binding.btnEnterReply.setOnClickListener{
            //댓글 입력 등록 버튼 처리
        }
    }

}
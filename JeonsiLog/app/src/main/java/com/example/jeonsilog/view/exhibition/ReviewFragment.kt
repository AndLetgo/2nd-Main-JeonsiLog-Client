package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.FragmentReviewBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.reply.ReplyRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.ReviewViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ReviewFragment : BaseFragment<FragmentReviewBinding>(R.layout.fragment_review) {
    private lateinit var exhibitionReplyRvAdapter: ExhibitionReplyRvAdapter
    private lateinit var replyList: MutableList<GetReplyInformation>
    private val reviewViewModel: ReviewViewModel by activityViewModels()
    private lateinit var reviewInfo: GetReviewsExhibitionInformationEntity
    private var replyPage = 0

    override fun init() {
        reviewInfo = reviewViewModel.reviewInfo.value!!
        binding.tvUserName.text = reviewInfo.nickname
        binding.brbExhibitionReview.rating = reviewInfo.rate.toFloat()
        binding.tvReviewContent.text = reviewInfo.contents
        binding.tvReplyCount.text = "${requireContext().getString(R.string.exhibition_reply)} ${reviewInfo.numReply}"
//        binding.tvReviewDate.text = reviewInfo.

        Glide.with(requireContext())
            .load(reviewInfo.imgUrl)
            .transform(CenterCrop(), RoundedCorners(80))
            .into(binding.ivProfile)

        //댓글 RecyclerView
        getReplyList()
        //recyclerView 페이징 처리
        binding.rvExhibitionReviewReply.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount){
                    setReplyRvByPage(totalCount)
                }
            }
        })

        binding.ibMenu.setOnClickListener {
            (activity as ExtraActivity).setMenuButton(it, parentFragmentManager)
        }

        binding.btnEnterReply.setOnClickListener{
            //댓글 입력 등록 버튼 처리
        }
    }

    private fun getReplyList(){
        replyList = mutableListOf()

        exhibitionReplyRvAdapter = ExhibitionReplyRvAdapter(replyList)
        binding.rvExhibitionReviewReply.adapter = exhibitionReplyRvAdapter
        binding.rvExhibitionReviewReply.layoutManager = LinearLayoutManager(this.context)

        setReplyRvByPage(0)

        exhibitionReplyRvAdapter.setOnItemClickListener(object : ExhibitionReplyRvAdapter.OnItemClickListener{
            override fun onMenuBtnClick(btn: View) {
                ExtraActivity().setMenuButton(btn, parentFragmentManager)
            }

        })
    }

    private fun setReplyRvByPage(totalCount:Int){
        Log.d("reply", "setReplyRvByPage: called")
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ReplyRepositoryImpl().getReply(GlobalApplication.encryptedPrefs.getAT(),reviewInfo.reviewId,replyPage)
            if(response.isSuccessful && response.body()!!.check){
                replyList.addAll(response.body()!!.information)
                addItemCount = response.body()!!.information.size
            }
        }
        val startPosition = totalCount + 1
        exhibitionReplyRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        replyPage++
    }
}
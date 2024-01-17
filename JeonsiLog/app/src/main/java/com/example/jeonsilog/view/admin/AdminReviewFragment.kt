package com.example.jeonsilog.view.admin

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.FragmentAdminReviewBinding
import com.example.jeonsilog.repository.reply.ReplyRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.AdminReviewReplyRvAdapter
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.viewmodel.UpdateReviewItem
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReviewId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminReviewFragment : BaseFragment<FragmentAdminReviewBinding>(R.layout.fragment_admin_review) {
    private lateinit var adminReviewReplyRvAdapter: AdminReviewReplyRvAdapter
    private val adminViewModel: AdminViewModel by activityViewModels()
    private var replyList = mutableListOf<GetReplyInformation>()
    private lateinit var reviewInfo: GetReviewsExhibitionInformationEntity
    private var replyPage = 0
    private var hasNextPage = true
    private var reviewItem:UpdateReviewItem? = null
    override fun init() {
        if(adminViewModel.reportReviewId.value!=null){
            reviewInfo = getReviewInfo(adminViewModel.reportReviewId.value!!)!!
        }else{
            reviewItem = adminViewModel.reviewItem.value
            reviewInfo = reviewItem?.item!!
        }

        setReviewUi(reviewInfo)

        //댓글 RecyclerView
        getReplyList()
        //recyclerView 페이징 처리
        binding.rvExhibitionReviewReply.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setReplyRvByPage(totalCount)
                }
            }
        })
    }
    private fun getReviewInfo(reviewId: Int):GetReviewsExhibitionInformationEntity?{
        var review: GetReviewsExhibitionInformationEntity? = null
        runBlocking(Dispatchers.IO){
            val response = ReviewRepositoryImpl().getReview(encryptedPrefs.getAT(), reviewId)
            if(response.isSuccessful && response.body()!!.check){
                review = response.body()?.information!!
            }
        }
        return review
    }
    private fun setReviewUi(review: GetReviewsExhibitionInformationEntity){
        binding.tvUserName.text = review.nickname
        binding.brbExhibitionReview.rating = review.rate.toFloat()
        binding.tvReviewContent.text = review.contents
        binding.tvReplyCount.text = "${requireContext().getString(R.string.exhibition_reply)} ${review.numReply}"
        binding.tvReviewDate.text = DateUtil().formatElapsedTime(review.createdDate)
        binding.tvBtnDelete.setOnClickListener {
            runBlocking(Dispatchers.IO){
                ReviewRepositoryImpl().deleteReview(encryptedPrefs.getAT(), review.reviewId)
            }
            adminViewModel.setDeletedReviewPosition(reviewItem?.position!!)
            Navigation.findNavController(it).popBackStack()
        }

        Glide.with(requireContext())
            .load(review.imgUrl)
            .transform(CenterCrop(), RoundedCorners(80))
            .into(binding.ivProfile)
    }
    private fun getReplyList(){
        adminReviewReplyRvAdapter = AdminReviewReplyRvAdapter(replyList, requireContext())
        binding.rvExhibitionReviewReply.adapter = adminReviewReplyRvAdapter
        binding.rvExhibitionReviewReply.layoutManager = LinearLayoutManager(this.context)

        setReplyRvByPage(0)

        adminReviewReplyRvAdapter.setOnItemClickListener(object : AdminReviewReplyRvAdapter.OnItemClickListener{
            override fun onDeleteBtnClick(btn: View, position: Int, replyId: Int) {
                runBlocking(Dispatchers.IO){
                    ReplyRepositoryImpl().deleteReply(encryptedPrefs.getAT(), replyId)
                }
                adminReviewReplyRvAdapter.removeItem(position)
                binding.rvExhibitionReviewReply.adapter = adminReviewReplyRvAdapter
            }
        })
    }
    private fun setReplyRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ReplyRepositoryImpl().getReply(encryptedPrefs.getAT(), newReviewId, replyPage)
            if(response.isSuccessful && response.body()!!.check){
                replyList.addAll(response.body()!!.information.data)
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        adminReviewReplyRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        replyPage++
    }
    
    //Back Button 눌렀을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(adminViewModel.reportReviewId.value!=null){
                    isAdminExhibitionOpen =false
                    (activity as MainActivity).setStateFcm(false)
                    adminViewModel.setReportReviewId(null)
                }
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}
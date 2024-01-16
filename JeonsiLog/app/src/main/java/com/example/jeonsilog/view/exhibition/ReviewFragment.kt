package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.reply.GetReplyInformation
import com.example.jeonsilog.data.remote.dto.reply.PostReplyRequest
import com.example.jeonsilog.data.remote.dto.reply.UserEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.FragmentReviewBinding
import com.example.jeonsilog.repository.reply.ReplyRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.view.otheruser.OtherUserFragment
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.DialogUtil
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReplyId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReviewId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import java.time.LocalDateTime
import java.util.Date

class ReviewFragment : BaseFragment<FragmentReviewBinding>(R.layout.fragment_review), DialogWithIllusInterface {
    private lateinit var exhibitionReplyRvAdapter: ExhibitionReplyRvAdapter
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private lateinit var replyList: MutableList<GetReplyInformation>
    private lateinit var reviewInfo: GetReviewsExhibitionInformationEntity
    private var replyPage = 0
    private var hasNextPage = true

    override fun init() {
        isRefresh.observe(this){
            if(it){
                (activity as ExtraActivity).refreshFragment(R.id.reviewFragment)
                isRefresh.value = false
            }
        }

        binding.vm = exhibitionViewModel
        if(extraActivityReference==4){
            Log.d("review", "init: newReviewId: $newReviewId, newReplyId: $newReplyId")
            //댓글 존재여부 체크
            val check = checkHasReply()
            Log.d("review", "init: checkHasReply(): $check")
            if(!check){
                Toast.makeText(requireContext(), getString(R.string.toast_notification_go_reply_exception), Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
        Log.d("review", "init: after has reply check")
        if(getReviewInfo(newReviewId)!=null){
            reviewInfo = getReviewInfo(newReviewId)!!
            setReviewUi(reviewInfo)
        }else{
            //알림에서 넘어왔는데 해당 감상평 삭제 됐을 때
            if(extraActivityReference==1){
                Toast.makeText(requireContext(), getString(R.string.toast_notification_go_review_exception), Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }

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

        binding.ibMenu.setOnClickListener {
            val popupMenu: PopupMenu
            if(reviewInfo.userId == GlobalApplication.encryptedPrefs.getUI()){
                popupMenu = DialogUtil().setMenuButton(it, 0)
                popupMenu.setOnMenuItemClickListener{
                    showCustomDialog("삭제_감상평", reviewInfo.reviewId,-1, 1)
                    false
                }
            }else{
                popupMenu = DialogUtil().setMenuButton(it, 1)
                popupMenu.setOnMenuItemClickListener{
                    showCustomDialog("신고_감상평", reviewInfo.reviewId,-1, 1)
                    false
                }
            }
            popupMenu.show()
        }

        binding.btnEnterReply.setOnClickListener{
            postReply() //댓글 입력 등록 버튼 처리
        }
        Glide.with(requireContext())
            .load(encryptedPrefs.getURL())
            .transform(CenterCrop(), RoundedCorners(80))
            .into(binding.ivInputProfile)

        exhibitionViewModel.replyCount.observe(this){
            binding.tvReplyCount.text = getString(R.string.exhibition_reply_count, exhibitionViewModel.replyCount.value)
        }
        binding.llUserProfileArea.setOnClickListener {
            if(reviewInfo.userId == encryptedPrefs.getUI()){

                val fragment = MyPageFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_nav_frame, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val fragment = OtherUserFragment(reviewInfo.userId, reviewInfo.nickname)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_nav_frame, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }


    }
    private fun getReviewInfo(reviewId: Int): GetReviewsExhibitionInformationEntity?{
        var newReview:GetReviewsExhibitionInformationEntity? =null
        runBlocking(Dispatchers.IO) {
            val response = ReviewRepositoryImpl().getReview(encryptedPrefs.getAT(), reviewId)
            if(response.isSuccessful && response.body()!!.check){
                newReview = response.body()!!.information
            }
        }
        return newReview
    }
    private fun setReviewUi(review: GetReviewsExhibitionInformationEntity){
        binding.tvUserName.text = review.nickname
        if(review.rate==null || review.rate==0.0){
            binding.brbExhibitionReview.visibility = View.GONE
        }else{
            binding.brbExhibitionReview.visibility = View.VISIBLE
            binding.brbExhibitionReview.rating = review.rate.toFloat()
        }
        binding.tvReviewContent.text = review.contents
        binding.tvReplyCount.text = "${requireContext().getString(R.string.exhibition_reply)} ${review.numReply}"
        exhibitionViewModel.setReplyCount(review.numReply)
        binding.tvReviewDate.text = DateUtil().formatElapsedTime(review.createdDate)

        Glide.with(requireContext())
            .load(review.imgUrl)
            .transform(CenterCrop(), RoundedCorners(80))
            .into(binding.ivProfile)
    }
    private fun getReplyList(){
        replyList = mutableListOf()

        exhibitionReplyRvAdapter = ExhibitionReplyRvAdapter(replyList, requireContext())
        binding.rvExhibitionReviewReply.adapter = exhibitionReplyRvAdapter
        binding.rvExhibitionReviewReply.layoutManager = LinearLayoutManager(this.context)

        setReplyRvByPage(0)

        exhibitionReplyRvAdapter.setOnItemClickListener(object : ExhibitionReplyRvAdapter.OnItemClickListener{
            override fun onMenuBtnClick(btn: View, user:Int, contentId: Int, position: Int) {
                val popupMenu = DialogUtil().setMenuButton(btn, user)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_delete -> {
                            showCustomDialog("삭제_댓글", contentId,position, -1)
                        }
                        else -> {
                            showCustomDialog("신고_댓글", contentId,position, -1)
                        }
                    }
                    false
                }
                popupMenu.show()
            }
        })
    }

    private fun setReplyRvByPage(totalCount:Int){
        Log.d("reply", "setReplyRvByPage: called")
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ReplyRepositoryImpl().getReply(encryptedPrefs.getAT(), newReviewId,replyPage)
            if(response.isSuccessful && response.body()!!.check){
                replyList.addAll(response.body()!!.information.data)
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        exhibitionReplyRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        replyPage++
    }

    private fun postReply(){
        var replyContent =""
        runBlocking(Dispatchers.IO) {
            replyContent = binding.etWritingReply.text.toString()
            val body = PostReplyRequest(reviewInfo.reviewId, replyContent)
            ReplyRepositoryImpl().postReply(encryptedPrefs.getAT(), body)
        }
        val createdDate = LocalDateTime.now()
        val newReply = GetReplyInformation(replyList.size, replyContent, createdDate.toString(), UserEntity(
            encryptedPrefs.getUI(), encryptedPrefs.getNN()!!, encryptedPrefs.getURL()!!
        ))
        replyList.add(newReply)
        binding.rvExhibitionReviewReply.adapter?.notifyItemInserted(replyList.size)
        binding.etWritingReply.setText("")

        exhibitionViewModel.setReplyCount(exhibitionViewModel.replyCount.value!!+1)
        if(exhibitionViewModel.reviewItem.value!=null){
            exhibitionViewModel.setReviewItemNumReply(true)
        }
    }

    fun showCustomDialog(type:String, contentId:Int,position:Int, reviewSide:Int) {
        val customDialogFragment = DialogWithIllus(type, contentId, reviewSide, position, this)
        customDialogFragment.show(parentFragmentManager, tag)
    }

    override fun confirmButtonClick(position: Int) {
        exhibitionReplyRvAdapter.removeItem(position)
        binding.rvExhibitionReviewReply.adapter = exhibitionReplyRvAdapter
        if(exhibitionViewModel.reviewItem.value!=null){
            exhibitionViewModel.setReviewItemNumReply(false)
        }
        exhibitionViewModel.setReplyCount(exhibitionViewModel.replyCount.value!!-1)

    }

    //Back Button 눌렀을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("TAG", "handleOnBackPressed: extraActivityReference: $extraActivityReference ")
                if(extraActivityReference == 1){
                    activity?.finish()
                }else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    //댓글 존재 여부 체크
    private fun checkHasReply():Boolean{
        var check = true
        runBlocking(Dispatchers.IO){
            val response = ReplyRepositoryImpl().getHasReply(encryptedPrefs.getAT(), newReplyId)
            if(response.isSuccessful && response.body()!!.check){
                check = response.body()!!.information.isExist
                Log.d("review", "checkHasReply: success $check", )
            }else{
                Log.e("review", "checkHasReply: fail", )
            }
        }
        return check
    }
}
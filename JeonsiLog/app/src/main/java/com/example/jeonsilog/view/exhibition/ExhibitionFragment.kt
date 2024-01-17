package com.example.jeonsilog.view.exhibition

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionInfo
import com.example.jeonsilog.data.remote.dto.rating.PostRatingRequest
import com.example.jeonsilog.data.remote.dto.review.CheckReviewEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.FragmentExhibitionBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.interest.InterestRepositoryImpl
import com.example.jeonsilog.repository.rating.RatingRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.viewmodel.UpdateReviewItem
import com.example.jeonsilog.widget.utils.DateUtil
import com.example.jeonsilog.widget.utils.DialogUtil
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceName
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReviewId
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.NullPointerException

class ExhibitionFragment : BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition), DialogWithIllusInterface{
    private lateinit var exhibitionRvAdapter: ExhibitionReviewRvAdapter
    private var exhibitionInfoData: ExhibitionInfo? = null
    private var thisExhibitionId = 0
    private var reviewPage = 0
    private var reviewList = mutableListOf<GetReviewsExhibitionInformationEntity>()
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()
    private var check = true
    private var hasNextPage = true
    private lateinit var preDrawListener: OnPreDrawListener


    override fun init() {
        isRefresh.observe(this){
            if(it){
                (activity as ExtraActivity).refreshFragment(R.id.exhibitionFragment)
                isRefresh.value = false
            }
        }

        val navController = Navigation.findNavController(binding.llExhibitionPlace)
        when(extraActivityReference){
            1 -> {
                navController.navigate(R.id.action_exhibitionFragment_to_reviewFragment)
            }
            2 -> {
                navController.navigate(R.id.action_exhibitionFragment_to_exhibitionPlaceFragment)
                extraActivityReference = 3
            }
        }
        if(exhibitionViewModel.currentExhibitionIds.value == null || exhibitionViewModel.getCurrentExhibitionsSize()<=0){
            thisExhibitionId = exhibitionId
        }else{
            thisExhibitionId = exhibitionViewModel.currentExhibitionIds.value!![exhibitionViewModel.getCurrentExhibitionsSize()-1]
        }
        Log.d("exhibitionID", "init: exhibitionID: ${thisExhibitionId}")

        getExhibitionInfo() //페이지 세팅
        setBottomSheet() //바텀시트 세팅

        //감상평 작성하고 돌아왔을 때, 댓글 입력 시 UI 다시 그리기
        if(exhibitionViewModel.userReview.value!=""){
            (activity as ExtraActivity).refreshFragment(R.id.exhibitionFragment)
            exhibitionViewModel.setUserReview("")
        }
        //감상평 - RecyclerView
        getReviewInfo()
        //감상평 작성하기
        binding.btnWritingReview.setOnClickListener{
            checkHasReview()
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_writingReviewFragment)
        }
        //댓글 입력하고 돌아왔을 때
        if(exhibitionViewModel.reviewItem.value!=null){
            val item = exhibitionViewModel.reviewItem.value!!
            exhibitionRvAdapter.replaceItem(item.item, item.position)
            binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        }
        //감상평 상세에서 삭제했을 때
        if(exhibitionViewModel.checkReviewDelete.value!! && exhibitionViewModel.myReviewItem.value!=null){
            exhibitionRvAdapter.removeItem(exhibitionViewModel.myReviewItem.value!!.position)
            exhibitionViewModel.setCheckReviewDelte(false)
        }

        //포스터
        binding.ivPosterImage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_posterFragment)
        }

        //전시장 상세정보
        binding.llExhibitionPlace.setOnClickListener {
            newPlaceId = exhibitionInfoData!!.place.placeId!!
            newPlaceName = exhibitionInfoData!!.place.placeName!!
            Navigation.findNavController(it).navigate(R.id.action_exhibitionFragment_to_exhibitionPlaceFragment)
        }

        //Interest
        if(exhibitionInfoData?.checkInterest !=null){
            if(exhibitionInfoData!!.checkInterest){
                binding.tbInterest.isChecked = true
            }
        }
        binding.tbInterest.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> {
                    runBlocking(Dispatchers.IO){
                        val response = InterestRepositoryImpl().postInterest(encryptedPrefs.getAT(), thisExhibitionId)
                        if(response.isSuccessful && response.body()!!.check){
                            Log.d("interest", "init: 등록 성공")
                        }
                    }
                }
                false ->{
                    runBlocking(Dispatchers.IO) {
                        val response = InterestRepositoryImpl().deleteInterest(encryptedPrefs.getAT(), thisExhibitionId)
                        if(response.isSuccessful && response.body()!!.check){
                            Log.d("interest", "init: 삭제 성공")
                        }
                    }
                }
            }
        }
        //Call
        binding.ibCall.setOnClickListener {
            //null 처리 필요
            if(exhibitionInfoData?.place?.tel !=null){
                val clipboardManager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("label", exhibitionInfoData?.place?.tel)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), "copy success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), getString(R.string.toast_exhibition_place_call_empty), Toast.LENGTH_SHORT).show()
            }
        }
        //Link
        binding.ibGoWeb.setOnClickListener {
            //null 처리 필요
            if(exhibitionInfoData?.place?.homePage != null){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(exhibitionInfoData?.place?.homePage))
                startActivity(intent)    
            }else{
                Toast.makeText(requireContext(), getString(R.string.toast_exhibition_place_homepage_empty), Toast.LENGTH_SHORT).show()
            }
        }

        //전시회 정보 더보기 버튼 처리
        binding.tvReadMoreInfo.setOnClickListener {
            if(check){
                binding.tvInformation.maxLines = Int.MAX_VALUE
                binding.tvReadMoreInfo.visibility = View.GONE
                check = !check
            }
        }
        binding.tvInformation.setOnClickListener {
            if (!check) {
                binding.tvInformation.maxLines = 3
                binding.tvReadMoreInfo.visibility = View.VISIBLE
                check = !check
            }
        }

        //recyclerView 페이징 처리
        binding.rvExhibitionReview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)
                if(rvPosition == totalCount && hasNextPage){
                    setReviewRvByPage(totalCount)
                }
            }
        })

        exhibitionViewModel.information.observe(this){
            runBlocking {
                binding.tvInformation.text = exhibitionViewModel.information.value
            }
            Log.d("TAG", "init: binding.tvInformation.lineCount: ${binding.tvInformation.lineCount }")
            if(binding.tvInformation.lineCount >=3 && check){
                binding.tvReadMoreInfo.visibility = View.VISIBLE
            }else{
                binding.tvReadMoreInfo.visibility = View.GONE
            }
        }
    }

    private fun setBottomSheet(){
        //디바이스 높이값 가져오기
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels

        //바텀시트 세팅
        BottomSheetBehavior.from(binding.nsvBottomSheet).apply {
            peekHeight = (screenHeight * 0.52).toInt()
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getExhibitionInfo(){
        check = true

        exhibitionInfoData = runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibition(encryptedPrefs.getAT(), thisExhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                response.body()!!.information
            }else{
                null
            }
        }

        if(!exhibitionInfoData?.imageUrl.isNullOrEmpty()){
            Glide.with(requireContext())
                .load(exhibitionInfoData?.imageUrl)
                .transform(CenterCrop())
                .into(binding.ivPosterImage)
        }else{
            Glide.with(requireContext())
                .load(R.drawable.illus_empty_poster)
                .transform(CenterInside())
                .into(binding.ivPosterImage)
        }

        binding.tvExhibitionName.text = exhibitionInfoData?.exhibitionName
        binding.tvAddress.text = exhibitionInfoData?.place?.address

        if(exhibitionInfoData?.place?.placeName !=null){
            binding.tvPlaceName.text = exhibitionInfoData?.place?.placeName
        }else{
            binding.llExhibitionPlace.visibility = View.GONE
        }

        var date = ""
        if(exhibitionInfoData?.startDate!=null){
            date = DateUtil().editStringDate(exhibitionInfoData!!.startDate) + " ~ " + DateUtil().editStringDate(exhibitionInfoData!!.endDate)
        }
        binding.tvDate.text = date

        setKeywords()

        //전시회 정보
        setExhibitionInformation(exhibitionInfoData?.information)
        setRatings()
    }

    private fun setExhibitionInformation(information: String?){
        //전시회 정보
        if(information!=null){
            exhibitionViewModel.setInformation(information)
        }else{
            Log.d("information", "getExhibitionInfo: null")
            binding.tvInfoTitle.visibility = View.GONE
            binding.tvInformation.visibility = View.GONE
            binding.tvReadMoreInfo.visibility = View.GONE
        }
    }
    private fun setRatings(){
        //평균 별점
        binding.tvRatingRate.text = "%.1f".format(exhibitionInfoData?.rate)
        //유저 별점
        if(exhibitionInfoData?.myRating!=null){
            binding.ratingBar.rating = exhibitionInfoData?.myRating!!
        }
        //별점 등록
        binding.ratingBar.setOnRatingChangeListener { _, rating, _ ->
            if(rating <= 0.5){
                runBlocking(Dispatchers.IO) {
                    val response = RatingRepositoryImpl().deleteRating(encryptedPrefs.getAT(), exhibitionId)
                    if(response.isSuccessful && response.body()!!.check){
                        Log.d("rating", "deleteRating: suceessful")
                    }
                }
            }else{
                runBlocking(Dispatchers.IO) {
                    val post = PostRatingRequest(thisExhibitionId, rating.toDouble())
                    val response = RatingRepositoryImpl().patchRating(encryptedPrefs.getAT(), post)
                    if(response.isSuccessful && response.body()!!.check){
                        Log.d("rating", "patchRating: suceessful")
                    }else{
                        runBlocking(Dispatchers.IO) {
                            val post = PostRatingRequest(thisExhibitionId, rating.toDouble())
                            val response = RatingRepositoryImpl().postRating(encryptedPrefs.getAT(), post)
                            if(response.isSuccessful && response.body()!!.check){
                                Log.d("rating", "postRating: suceessful")
                            }
                        }
                    }
                }
            }
            regetExhibitionRate()

            if(exhibitionViewModel.myReviewItem.value!=null){
                val review = exhibitionViewModel.myReviewItem.value
                review!!.item.rate = rating.toDouble()
                exhibitionViewModel.setMyReviewItem(review)
                exhibitionRvAdapter.replaceItem(review.item, review.position)
            }
        }
    }
    private fun regetExhibitionRate(){
        exhibitionInfoData = runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibition(encryptedPrefs.getAT(), thisExhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                response.body()!!.information
            }else{
                null
            }
        }
        binding.tvRatingRate.text = "%.1f".format(exhibitionInfoData?.rate)
    }
    private fun setKeywords(){
        var operatingKeyword = ""
        when(exhibitionInfoData?.operatingKeyword){
            "ON_DISPLAY" -> operatingKeyword = requireContext().getString(R.string.keyword_state_on)
            "BEFORE_DISPLAY" -> operatingKeyword = requireContext().getString(R.string.keyword_state_before)
        }
        var priceKeyword = ""
        when(exhibitionInfoData?.priceKeyword){
            "FREE" -> priceKeyword = requireContext().getString(R.string.keyword_free)
            else -> binding.tvKeywordSecond.isGone = true
        }

        if(operatingKeyword!=""){
            binding.tvKeywordFirst.text = operatingKeyword
            binding.tvKeywordSecond.text = priceKeyword
        }else{
            if(priceKeyword!=""){
                binding.tvKeywordSecond.isGone = true
                binding.tvKeywordFirst.text = priceKeyword
            }else {
                binding.tvKeywordFirst.isGone = true
            }
        }
    }
    private fun getReviewInfo(){
        Log.d("TAG", "getReviewInfo: 실행 $reviewPage")
        exhibitionRvAdapter = ExhibitionReviewRvAdapter(reviewList, requireContext())
        binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        binding.rvExhibitionReview.layoutManager = LinearLayoutManager(context)

        setReviewRvByPage(0)

        exhibitionRvAdapter.setOnItemClickListener(object: ExhibitionReviewRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetReviewsExhibitionInformationEntity, position: Int) {
                //감상평 페이지로 이동
                val item = UpdateReviewItem(data, position)
                exhibitionViewModel.setReviewItem(item)
                newReviewId = data.reviewId
                Navigation.findNavController(v).navigate(R.id.action_exhibitionFragment_to_reviewFragment)
            }

            override fun onMenuBtnClick(btn: View, user: Int, contentId: Int, position: Int) {
                val popupMenu = DialogUtil().setMenuButton(btn, user)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_delete -> {
                            showCustomDialog("삭제_감상평", contentId,position)
                        }
                        else -> {
                            showCustomDialog("신고_감상평", contentId,position)
                        }
                    }
                    false
                }
                popupMenu.show()
            }

            override fun saveUserReview(data: GetReviewsExhibitionInformationEntity, position: Int) {
                val item = UpdateReviewItem(data, position)
                exhibitionViewModel.setMyReviewItem(item)
            }
        })
    }
    private fun setReviewRvByPage(totalCount:Int){
        var addItemCount = 0
        Log.d("review", "setReviewRvByPage: review Page: $reviewPage")
        runBlocking(Dispatchers.IO) {
            val response = ReviewRepositoryImpl().getReviews(encryptedPrefs.getAT(), thisExhibitionId, reviewPage)
            if(response.isSuccessful && response.body()!!.check){
                reviewList.addAll(response.body()!!.informationEntity.data)
                addItemCount = response.body()!!.informationEntity.data.size
                hasNextPage = response.body()!!.informationEntity.hasNextPage
            }else{
                null
            }
        }
        exhibitionRvAdapter.notifyItemRangeInserted(totalCount, addItemCount)
        reviewPage++
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "onDestroyView: ")
    }

    //Back Button 눌렀을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("tag", "onAttach Back")
                view?.let { exhibitionViewModel.removeLastExhibitionId() }
                exhibitionViewModel.resetMyReviewItem()
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    //삭제하기
    override fun confirmButtonClick(position: Int) {
        exhibitionRvAdapter.removeItem(position)
        binding.rvExhibitionReview.adapter = exhibitionRvAdapter
    }

    fun showCustomDialog(type:String, contentId:Int,position:Int) {
        val customDialogFragment = DialogWithIllus(type, contentId, 0, position, this)
        customDialogFragment.show(parentFragmentManager, tag)
    }

    //기존 감상평 있는지 체크하고 있으면 띄우기
    private fun checkHasReview(){
        var checkReviewEntity : CheckReviewEntity? = null
        runBlocking(Dispatchers.IO){
            val response = ReviewRepositoryImpl().getCheckHasReview(encryptedPrefs.getAT(), thisExhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                checkReviewEntity = response.body()!!.information
            }
        }
        if(checkReviewEntity!=null){
            exhibitionViewModel.setCheckReviewEntity(checkReviewEntity!!)
        }
    }
}
package com.example.jeonsilog.view.admin

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionInfo
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionRequest
import com.example.jeonsilog.data.remote.dto.exhibition.UpdatePlaceInfoEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.databinding.FragmentAdminExhibitionBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.viewmodel.UpdateReviewItem
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newReviewId
import com.example.jeonsilog.widget.utils.ImageUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdminExhibitionFragment : BaseFragment<FragmentAdminExhibitionBinding>(R.layout.fragment_admin_exhibition){
    private lateinit var exhibitionRvAdapter: AdminExhibitionReviewRvAdapter
    private var exhibitionInfoData: ExhibitionInfo? = null
    private var thisExhibitionId = 0
    private var hasNextPage = true
    val TAG = "report"
    //감상평
    private var reviewList = mutableListOf<GetReviewsExhibitionInformationEntity>()
    private var reviewPage = 0
    private val adminViewModel: AdminViewModel by activityViewModels()

    override fun init() {
        (activity as MainActivity).setStateToolBar(false)

        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragmentInAdmin(R.id.adminExhibitionFragment)
                isRefresh.value = false
            }
        }
        (activity as MainActivity).setStateBn(false, "admin")

        //신고 -> 포스터
        Log.d(TAG, "init: adminViewModel.reportExhibitionId.value: ${adminViewModel.reportExhibitionId.value}")
        if(adminViewModel.reportExhibitionId.value!=null){
            thisExhibitionId = adminViewModel.reportExhibitionId.value!!
            exhibitionId = adminViewModel.reportExhibitionId.value!!
        }else{
            thisExhibitionId = exhibitionId
        }

        setBottomSheet() //바텀시트 세팅

        binding.vm = adminViewModel
        binding.lifecycleOwner = this

        //감상평 - RecyclerView
        if(adminViewModel.isChanged.value!!){
            reloadExhibitionInfo()
        }else{
            getExhibitionInfo()
        }
        getReviewInfo()
        //감상평 삭제 시
        if(adminViewModel.deletedReviewPosition.value != null){
            deleteReview(adminViewModel.deletedReviewPosition.value!!)
        }
        //댓글 삭제하고 돌아왔을 때
        if(adminViewModel.reviewItem.value!=null){
            val item = adminViewModel.reviewItem.value!!
            exhibitionRvAdapter.replaceItem(item.item, item.position)
            binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        }

        //전시회 이름
        adminViewModel.exhibitionName.observe(this){
            binding.tvExhibitionName.text = adminViewModel.exhibitionName.value
        }
        binding.tvExhibitionName.setOnClickListener {
            showCustomDialog("exhibitionName", adminViewModel.exhibitionInfo.value!!.exhibitionName)
        }

        //전시공간 이름
        adminViewModel.placeName.observe(this){
            binding.tvPlaceName.text = if(!adminViewModel.placeName.value.isNullOrEmpty()){
                adminViewModel.placeName.value
            }else{

                getString(R.string.admin_exhibition_place_name_empty)
            }
        }
        binding.tvPlaceName.setOnClickListener {
            if(adminViewModel.exhibitionInfo.value!!.place.placeName !=null){
                showCustomDialog("placeName", adminViewModel.exhibitionInfo.value!!.place.placeName!!)
            }else{
                showCustomDialog("placeName", null)
            }
        }

        //전시공간 주소
        adminViewModel.placeAddress.observe(this){
            binding.tvAddress.text = if(!adminViewModel.placeAddress.value.isNullOrEmpty()){
                adminViewModel.placeAddress.value
            }else{
                getString(R.string.admin_exhibition_place_address_empty)
            }
        }
        binding.tvAddress.setOnClickListener {
            if(adminViewModel.exhibitionInfo.value!!.place.address !=null){
                showCustomDialog("placeAddress", adminViewModel.exhibitionInfo.value!!.place.address!!)
            }else{
                showCustomDialog("placeAddress", null)
            }
        }

        //전시공간 번호
        adminViewModel.placeCall.observe(this){
            binding.tvCall.text = if(!adminViewModel.placeCall.value.isNullOrEmpty()){
                adminViewModel.placeCall.value
            }else{
                getString(R.string.admin_exhibition_place_call_empty)
            }
        }
        binding.tvCall.setOnClickListener {
            if(adminViewModel.exhibitionInfo.value!!.place.tel!=null){
                showCustomDialog("placeCall", adminViewModel.exhibitionInfo.value!!.place.tel!!)
            }else{
                showCustomDialog("placeCall", null)
            }
        }

        //전시공간 홈페이지
        adminViewModel.placeHomepage.observe(this){
            binding.tvHomepage.text = if(!adminViewModel.placeHomepage.value.isNullOrEmpty()){
                adminViewModel.placeHomepage.value
            }else{
                getString(R.string.admin_exhibition_place_homepage_empty)
            }
        }
        binding.tvHomepage.setOnClickListener {
            if(adminViewModel.exhibitionInfo.value!!.place.homePage!=null){
                showCustomDialog("placeHomepage", adminViewModel.exhibitionInfo.value!!.place.homePage!!)
            }else{
                showCustomDialog("placeHomepage", null)
            }
        }

        //전시회 정보
        adminViewModel.exhibitionInformation.observe(this){
            binding.tvInformation.text = if(!adminViewModel.exhibitionInformation.value.isNullOrEmpty()){
                adminViewModel.exhibitionInformation.value
            }else{
                getString(R.string.admin_exhibition_information_empty)
            }
        }
        binding.tvInformation.setOnClickListener {
            if(adminViewModel.exhibitionInfo.value!!.information !=null){
                showCustomDialog("exhibitionInformation", adminViewModel.exhibitionInfo.value!!.information!!)
            }else{
                showCustomDialog("exhibitionInformation", null)
            }
        }

        //포스터
        binding.ivPosterImage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminExhibitionFragment_to_adminPosterFragment)
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

        //수정한 정보 저장
        binding.btnSaveAll.setOnClickListener {
            if(adminViewModel.isChanged.value!!){
                saveEditInformations()
            }
        }

        binding.ibKeywordOperating.setOnClickListener {
            binding.llKeywordOperating.visibility = View.GONE
            val item = adminViewModel.exhibitionInfo.value!!
            item.operatingKeyword = OperatingKeyword.AFTER_DISPLAY.name
            adminViewModel.setExhibitionInfo(item)
            adminViewModel.setIsChanged(true)
        }
        binding.ibKeywordPrice.setOnClickListener {
            binding.llKeywordPrice.visibility = View.GONE
            val item = adminViewModel.exhibitionInfo.value!!
            item.priceKeyword = PriceKeyword.PAY.name
            adminViewModel.setExhibitionInfo(item)
            adminViewModel.setIsChanged(true)
        }

        binding.ibKeywordAdd.setOnClickListener {
            showAddKeywordMenu()
        }

        //신고 -> 포스터
        if(adminViewModel.reportExhibitionId.value!=null){
            Log.d(TAG, "init: go poster")
            Navigation.findNavController(binding.ivPosterImage).navigate(R.id.action_adminExhibitionFragment_to_adminPosterFragment)
            adminViewModel.setReportExhibitionId(null)
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
        exhibitionInfoData = runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibition(encryptedPrefs.getAT(), thisExhibitionId)
            if(response.isSuccessful && response.body()!!.check){
                response.body()!!.information
            }else{
                null
            }
        }
        if(exhibitionInfoData==null){
            return
        }
        adminViewModel.setExhibitionInfo(exhibitionInfoData!!)
        //포스터
        if(exhibitionInfoData?.imageUrl!=null){
            adminViewModel.setExhibitionPosterImg(exhibitionInfoData?.imageUrl!!)
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

        //전시회 이름
        adminViewModel.setExhibitionName(exhibitionInfoData?.exhibitionName!!)

        //전시공간 이름
        if(exhibitionInfoData?.place?.placeName !=null){
            adminViewModel.setPlaceName(exhibitionInfoData?.place?.placeName!!)
        }else{ adminViewModel.setPlaceName(getString(R.string.admin_exhibition_place_name_empty)) }


        //전시공간 주소
        if(exhibitionInfoData?.place?.address !=null){
            adminViewModel.setAddress(exhibitionInfoData?.place?.address!!)
        }else{ adminViewModel.setAddress(getString(R.string.admin_exhibition_place_address_empty)) }

        //전시공간 전화번호
        if(exhibitionInfoData?.place?.tel !=null){
            adminViewModel.setPlaceCall(exhibitionInfoData?.place?.tel!!)
        }else{ adminViewModel.setPlaceCall(getString(R.string.admin_exhibition_place_call_empty)) }

        //전시공간 홈페이지
        if(exhibitionInfoData?.place?.homePage !=null){
            adminViewModel.setPlaceHomepage(exhibitionInfoData?.place?.homePage!!)
        }else{ adminViewModel.setPlaceHomepage(getString(R.string.admin_exhibition_place_homepage_empty)) }

        //전시회 정보
        if(exhibitionInfoData?.information !=null){
            adminViewModel.setExhibitionInformation(exhibitionInfoData?.information!!)
        }else{ adminViewModel.setExhibitionInformation(getString(R.string.admin_exhibition_information_empty)) }


        setKeywords()
    }

    private fun setKeywords(){
        var operatingKeyword = ""
        when(exhibitionInfoData?.operatingKeyword){
            "ON_DISPLAY" -> operatingKeyword = requireContext().getString(R.string.keyword_state_on)
            "BEFORE_DISPLAY" -> operatingKeyword = requireContext().getString(R.string.keyword_state_before)
            else -> binding.llKeywordOperating.visibility = View.GONE
        }
        if(operatingKeyword!=""){
            binding.llKeywordOperating.visibility = View.VISIBLE
            binding.tvKeywordOperating.text = operatingKeyword
        }
        when(exhibitionInfoData?.priceKeyword){
            "FREE" -> {
                binding.llKeywordPrice.visibility = View.VISIBLE
                binding.tvKeywordPrice.text = getString(R.string.keyword_free)
            }
            else -> binding.llKeywordPrice.visibility = View.GONE
        }
    }

    //감상평 RecyclerView
    private fun getReviewInfo(){
        exhibitionRvAdapter = AdminExhibitionReviewRvAdapter(reviewList, requireContext())
        binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        binding.rvExhibitionReview.layoutManager = LinearLayoutManager(context)

        setReviewRvByPage(0)

        exhibitionRvAdapter.setOnItemClickListener(object: AdminExhibitionReviewRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetReviewsExhibitionInformationEntity, position: Int) {
                //감상평 페이지로 이동
                val item = UpdateReviewItem(data, position)
                adminViewModel.setReviewItem(item)
                newReviewId = data.reviewId
                Navigation.findNavController(v).navigate(R.id.action_adminExhibitionFragment_to_adminReviewFragment)
            }

            override fun deleteItem(position: Int, reviewId: Int) {
                var deleteSuccess =false
                runBlocking(Dispatchers.IO){
                    val response = ReviewRepositoryImpl().deleteReview(encryptedPrefs.getAT(), reviewId)
                    if(response.isSuccessful && response.body()!!.check){
                        deleteSuccess = true
                    }
                }
                if(deleteSuccess){
                    exhibitionRvAdapter.deleteItem(position)
                }
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
        exhibitionRvAdapter.notifyItemRangeInserted(totalCount+1, addItemCount)
        reviewPage++
    }
    private fun reloadExhibitionInfo(){
        //포스터
        if(adminViewModel.posterUri.value!=null){
            Glide.with(requireContext())
                .load(adminViewModel.posterUri.value)
                .into(binding.ivPosterImage)
        }else if(adminViewModel.exhibitionPosterImg.value!=null){
            Glide.with(requireContext())
                .load(adminViewModel.exhibitionPosterImg.value)
                .into(binding.ivPosterImage)
        }else{
            Glide.with(requireContext())
                .load(R.drawable.illus_empty_poster)
                .into(binding.ivPosterImage)
        }
        //정보
        binding.tvExhibitionName.text = adminViewModel.exhibitionName.value
        binding.tvPlaceName.text = if(adminViewModel.placeName.value!=null){
            adminViewModel.placeName.value
        }else{
            getString(R.string.admin_exhibition_place_name_empty)
        }
        binding.tvAddress.text = if(adminViewModel.placeAddress.value!=null){
            adminViewModel.placeAddress.value
        }else{
            getString(R.string.admin_exhibition_place_address_empty)
        }
        binding.tvCall.text = if(adminViewModel.placeCall.value!=null){
            adminViewModel.placeCall.value
        }else{
            getString(R.string.admin_exhibition_place_call_empty)
        }
        binding.tvHomepage.text = if(adminViewModel.placeHomepage.value!=null){
            adminViewModel.placeHomepage.value
        }else{
            getString(R.string.admin_exhibition_place_homepage_empty)
        }
        binding.tvInformation.text = if(adminViewModel.exhibitionInformation.value!=null){
            adminViewModel.exhibitionInformation.value
        }else{
            getString(R.string.admin_exhibition_information_empty)
        }
    }

    //수정사항 저장
    private fun saveEditInformations(){
        var isSuccess = false
        var checkPosterChange =false

        var filePart:MultipartBody.Part?
        if(adminViewModel.posterUri.value!=null){
            filePart = uriToMultipart(adminViewModel.posterUri.value!!)
            checkPosterChange = true
        }else if(adminViewModel.exhibitionPosterImg.value!=null){
            filePart = createImagePartFromUrl(adminViewModel.exhibitionPosterImg.value!!, "img")!!
        }else{
            filePart = setDefalutImage()
            checkPosterChange = true
        }

        val updateExhibitionDetailReq = PatchExhibitionRequest(
            exhibitionInfoData?.exhibitionId!!,
            adminViewModel.exhibitionInfo.value!!.exhibitionName,
            adminViewModel.exhibitionInfo.value!!.operatingKeyword,
            adminViewModel.exhibitionInfo.value!!.priceKeyword,
            adminViewModel.exhibitionInfo.value!!.information,
            checkPosterChange,
            UpdatePlaceInfoEntity(
                adminViewModel.exhibitionInfo.value!!.place.placeId,
                adminViewModel.exhibitionInfo.value!!.place.placeName,
                adminViewModel.exhibitionInfo.value!!.place.address,
                adminViewModel.exhibitionInfo.value!!.place.tel,
                adminViewModel.exhibitionInfo.value!!.place.homePage
            ))
        val requestJson = Gson().toJson(updateExhibitionDetailReq)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(),requestJson)

        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().patchExhibition(encryptedPrefs.getAT(), requestBody, filePart)
            if(response.isSuccessful && response.body()!!.check){
                isSuccess = true
            }else{
            }
        }
        if(isSuccess){
            Toast.makeText(requireContext(), getString(R.string.toast_exhibition_update), Toast.LENGTH_SHORT).show()
            if(adminViewModel.isReport.value!!){
                (activity as MainActivity).setStateFcm(false)
                Navigation.findNavController(binding.btnSaveAll).popBackStack()
                (activity as MainActivity).setStateBn(true, "admin")
                adminViewModel.setIsReport(false)
            }else{
                Navigation.findNavController(binding.btnSaveAll).popBackStack()
            }
        }
    }
    private fun uriToMultipart(uri: Uri): MultipartBody.Part{
        val file = File(ImageUtil().absolutelyPath(requireContext(), uri))
        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("img", file.name, imageRequestBody)
        return filePart
    }
    private fun showCustomDialog(type:String, text: String?) {
        val customDialogFragment = DialogAdmin(type, text)
        customDialogFragment.show(parentFragmentManager, tag)
    }

    private fun deleteReview(position:Int){
        exhibitionRvAdapter.deleteItem(position)
        binding.rvExhibitionReview.adapter = exhibitionRvAdapter
        adminViewModel.setDeletedReviewPosition(null)
    }

    //imageUrl -> MultipartBody.Part
    private fun downloadImageFromUrl(imageUrl: String, destinationFile: File): File {
        val url = URL(imageUrl)

        runBlocking(Dispatchers.IO){
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val outputStream = FileOutputStream(destinationFile)

                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.close()
                inputStream.close()
            }
        }

        return destinationFile
    }
    private fun createImagePartFromUrl(imageUrl: String, partName: String): MultipartBody.Part? {
        try {
            val destinationFile = File.createTempFile("tempImage", ".jpg")
            downloadImageFromUrl(imageUrl, destinationFile)

            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), destinationFile)
            destinationFile.deleteOnExit()

            return MultipartBody.Part.createFormData(partName, destinationFile.name, requestFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // null 이미지
    private fun setDefalutImage():MultipartBody.Part {
        val outputDir: File = requireActivity().cacheDir
        val outputFile: File = File.createTempFile("prefix", "suffix", outputDir)

        val requestFile: RequestBody = outputFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("image", null, requestFile)

        outputFile.deleteOnExit()
        return imagePart
    }

    //키워드
    private fun showAddKeywordMenu(){
        val popupMenu = PopupMenu(requireContext(), binding.ibKeywordAdd)
        popupMenu.menuInflater.inflate(R.menu.menu_admin_keywords, popupMenu.menu)
        popupMenu.menu.getItem(0).setActionView(R.layout.item_popup_menu)

        popupMenu.setOnMenuItemClickListener{
            addKeyword(it)
            false
        }
        popupMenu.show()
    }
    private fun addKeyword(it:MenuItem){
        var hasKeyword = false
        val item = adminViewModel.exhibitionInfo.value!!
        var newKeyword = ""
        when(it.itemId){
            R.id.menu_keyword_free -> {
                if(item.priceKeyword == PriceKeyword.FREE.name){
                    hasKeyword = true
                }else{
                    item.priceKeyword = PriceKeyword.FREE.name
                    binding.llKeywordPrice.visibility = View.VISIBLE
                    binding.tvKeywordPrice.text = getString(R.string.keyword_free)
                }
            }
            R.id.menu_keyword_state_before -> {
                if(item.operatingKeyword == OperatingKeyword.BEFORE_DISPLAY.name){
                    hasKeyword = true
                }else{
                    item.operatingKeyword = OperatingKeyword.BEFORE_DISPLAY.name
                    newKeyword = getString(R.string.keyword_state_before)
                }
            }
            R.id.menu_keyword_state_on -> {
                if(item.operatingKeyword == OperatingKeyword.ON_DISPLAY.name){
                    hasKeyword = true
                }else{
                    item.operatingKeyword = OperatingKeyword.ON_DISPLAY.name
                    newKeyword = getString(R.string.keyword_state_on)
                }
            }
        }
        if(hasKeyword){
            Toast.makeText(requireContext(), getString(R.string.toast_admin_keyword_has), Toast.LENGTH_SHORT).show()
        }else{
            if(newKeyword != ""){
                binding.llKeywordOperating.visibility = View.VISIBLE
                binding.tvKeywordOperating.text = newKeyword
            }
        }
        adminViewModel.setExhibitionInfo(item)
        adminViewModel.setIsChanged(true)
    }

    //Back Button 눌렀을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(adminViewModel.isReport.value!!){
                    (activity as MainActivity).setStateFcm(false)
                    Navigation.findNavController(binding.btnSaveAll).popBackStack()
                    (activity as MainActivity).setStateBn(true, "admin")
                    isAdminExhibitionOpen = false
                    adminViewModel.setIsReport(false)
                }else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}
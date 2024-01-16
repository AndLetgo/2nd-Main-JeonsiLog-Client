package com.example.jeonsilog.view.photocalendar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.data.remote.dto.exhibition.GetCalendarExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchPlaceEntity
import com.example.jeonsilog.databinding.ViewLoadPageDialogBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDate


class LoadPageDialog(private var selectedDate: LocalDate,private val listener: CommunicationListener,private var myEditText:String?) : DialogFragment() {
    private var _binding: ViewLoadPageDialogBinding? = null
    private val binding get() = _binding!!
    val regexPattern = Regex("[!@#\\\$%^&*(),.?\\\":{}|<>;]")
    private lateinit var loadPageRvAdapter: LoadPageRvAdapter
    private var loadPageRvList = mutableListOf<SearchInformationEntity>()
    private var exhibitionPage = 0
    private var countItem = 0
    private var hasNextPage = true
    private var hasMaxPage = true
    private var edittext=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewLoadPageDialogBinding.inflate(inflater, container, false)
        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(LoadPageDialog(selectedDate,listener,myEditText))
                isRefresh.value = false
            }
        }

        return binding.root
    }

    fun checkEmptyListFalse(){
        binding.ivEmptyLoad.isVisible=false
        binding.tvEmptyLoad01.isVisible=false
        binding.tvEmptyLoad02.isVisible=false
    }
    fun checkEmptyListTrue(){
        binding.ivEmptyLoad.setBackgroundResource(R.drawable.illus_empty_search)
        binding.ivEmptyLoad.isVisible=true
        binding.tvEmptyLoad01.isVisible=true
        binding.tvEmptyLoad01.setText(R.string.empty_search_title)
        binding.tvEmptyLoad02.isVisible=true
        binding.tvEmptyLoad02.setText(R.string.empty_search_description)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그의 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 다이얼로그의 외부 터치 이벤트 처리 (다이얼로그가 닫히지 않도록 함)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 뒤로가기 버튼이 눌렸을 때 수행할 동작
                dismiss()
                true
            } else {
                false
            }
        }

        // 전체 화면으로 다이얼로그 표시 (다이얼로그 내용물의 크기를 외부까지 확장)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
        // 다이얼로그의 내용물이 차지하는 레이아웃의 크기를 조정
        val params = binding.root.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.root.layoutParams = params
        binding.ivRecordDelete.isGone=true


        binding.rvLoadPage.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = (recyclerView.adapter?.itemCount)?.div(2)
                val tttt=(recyclerView.adapter?.itemCount)?.div(2)
                Log.d("mymymymy", "${rvPosition} $tttt")
                if(rvPosition == totalCount && hasNextPage){
                    hasMaxPage=true

                    setExhibitionRvByShortPage()

                }
            }
        })






        setEditBoxDeleteBt()
        setOnEditorActionListener()

        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.window?.setBackgroundDrawable(null)
        showKeyboard()
    }

    fun setOnEditorActionListener(){
        binding.etLoadPage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                myEditText = binding.etLoadPage.text.toString()

                if(myEditText!!.isBlank()) {
                    Toast.makeText(context, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()

                }else if(regexPattern.containsMatchIn(myEditText!!)&&myEditText!!.length<=2){
                    Toast.makeText(context, "해당 검색어는 검색할수 없어요", Toast.LENGTH_SHORT).show()
                }else{
                    loadPageRvList = mutableListOf<SearchInformationEntity>()
                    edittext= myEditText as String
                    loadPageRvAdapter = LoadPageRvAdapter(requireContext(),loadPageRvList,selectedDate,listener,this@LoadPageDialog)
                    binding.rvLoadPage.adapter = loadPageRvAdapter
                    binding.rvLoadPage.layoutManager = LinearLayoutManager(requireContext())
                    exhibitionPage=0
                    countItem=0
                    hasNextPage = true
                    hasMaxPage = true
                    setExhibitionRvByPage()
                    hideSoftKeyboard()
                }
                return@setOnEditorActionListener true
            }
            false
        }

    }
    private fun setExhibitionRvByPage(){
        loadList()
        if(loadPageRvList.size==0){
            checkEmptyListTrue()
        }else{
            checkEmptyListFalse()
        }
        loadPageRvAdapter.notifyDataSetChanged()
    }
    fun loadList(){
        if (hasNextPage && hasMaxPage){
            runBlocking(Dispatchers.IO) {
                val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,exhibitionPage)
                if(response.isSuccessful && response.body()!!.check){
                    if(exhibitionPage==0){
                        loadPageRvList.clear()
                    }
                    val dataList = response.body()!!.information.data
                    var index = 0
                    while (index < dataList.size) {
                        val item = dataList[index]
                        if (item.exhibitionName.contains(edittext)) {
                            loadPageRvList.add(item)
                        }
                        index++
                    }
                    exhibitionPage++
                    hasNextPage = response.body()!!.information.hasNextPage
                    Log.d("mymymymy", "loadList :$countItem ${loadPageRvList.size}")
                    if(countItem<loadPageRvList.size/20){
                        countItem++
                        hasMaxPage=false
                    }else{
                        hasMaxPage=true
                    }
                }
                else{
                    hasNextPage = false
                }
            }
            loadList()
        }

    }
    private fun setExhibitionRvByShortPage(){
        loadShortList()

        loadPageRvAdapter.notifyDataSetChanged()
    }
    fun loadShortList(){
        if (hasNextPage && hasMaxPage){
            runBlocking(Dispatchers.IO) {
                val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,exhibitionPage)
                if(response.isSuccessful && response.body()!!.check){

                    val dataList = response.body()!!.information.data
                    var index = 0
                    while (index < dataList.size) {
                        val item = dataList[index]
                        if (item.exhibitionName.contains(edittext)) {
                            loadPageRvList.add(item)
                        }
                        index++
                    }
                    exhibitionPage++
                    hasNextPage = response.body()!!.information.hasNextPage
                    Log.d("mymymymy", "loadShortList :$countItem ${loadPageRvList.size}")
                    if(countItem<loadPageRvList.size/5){
                        countItem++
                        hasMaxPage=false
                    }else{
                        hasMaxPage=true
                    }
                }
                else{
                    hasNextPage = false
                }
            }
        }
    }
    fun setEditBoxDeleteBt(){
        //edittext x 버튼 제어
        binding.etLoadPage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 전 동작
            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 동작 (실시간 감지)
                val newText = charSequence.toString()
                updateClearButtonVisibility(charSequence?.isNotEmpty() == true)
                // 여기에 텍스트가 변경될 때 수행할 동작을 추가
            }
            override fun afterTextChanged(editable: Editable?) {
                // 텍스트 변경 후 동작
            }
        })
        binding.ivRecordDelete.setOnClickListener {
            binding.etLoadPage.text.clear()
        }
    }
    private fun updateClearButtonVisibility(show: Boolean) {
        //텍스트 삭제버튼 visibility
        binding.ivRecordDelete.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun hideSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    private fun showKeyboard() {
        view?.postDelayed({
            Log.d("TAG", "showKeyboard: ")
            binding.etLoadPage.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etLoadPage, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
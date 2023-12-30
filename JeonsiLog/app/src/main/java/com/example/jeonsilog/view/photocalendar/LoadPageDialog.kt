package com.example.jeonsilog.view.photocalendar

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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.databinding.ViewLoadPageDialogBinding
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel


class LoadPageDialog(private val viewModel: PhotoCalendarViewModel,var cellDate:String) : DialogFragment(),OnItemListener02 {

    private var _binding: ViewLoadPageDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewLoadPageDialogBinding.inflate(inflater, container, false)
        setEmptyView()
        return binding.root
    }

    fun setEmptyView(){
        binding.ivEmptyLoad.isGone=true
        binding.tvEmptyLoad01.isGone=true
        binding.tvEmptyLoad02.isGone=true
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
        setEditBoxDeleteBt()
        setOnEditorActionListener()
    }

    fun setOnEditorActionListener(){
        binding.etLoadPage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {

                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                var enteredText = binding.etLoadPage.text.toString()
                if(enteredText.isBlank()) {
                    Toast.makeText(context, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                }else{
                    setLayoutView(findItemIndicesByExhibitionName(viewModel.exhibitionlist,enteredText))
                    hideSoftKeyboard()
                }


                return@setOnEditorActionListener true
            }
            false
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
    fun findItemIndicesByExhibitionName(exhibitionList: List<Test_Item>, search: String): List<Int> {
        val indices = mutableListOf<Int>()
        for ((index, item) in exhibitionList.withIndex()) {
            if (item.exhibitionName?.contains(search, ignoreCase = true) == true ||
                item.exhibitionLocation?.contains(search, ignoreCase = true) == true ||
                item.exhibitionPlace?.contains(search, ignoreCase = true) == true ||
                item.exhibitionPrice?.contains(search, ignoreCase = true) == true ||
                item.exhibitionDate?.contains(search, ignoreCase = true) == true
            ) {
                indices.add(index)
            }
        }
        return indices
    }
    fun setLayoutView(itemlist: List<Int>){
        setEmptyView()
        //리사이클러뷰 제어
        binding.rvLoadPage.layoutManager = LinearLayoutManager(requireContext())
        var items=extractItemsByIndices(viewModel.exhibitionlist,itemlist)
        val adapter = context?.let { LoadPageRvAdapter(it,items,viewModel,this,cellDate) }
        binding.rvLoadPage.adapter = adapter
        Log.d("TAG", "$items: ")
        if(items.size==0){
            binding.ivEmptyLoad.isGone=false
            binding.tvEmptyLoad01.isGone=false
            binding.tvEmptyLoad02.isGone=false
        }
    }

    fun extractItemsByIndices(exhibitionList: List<Test_Item>, indices: List<Int>): List<Test_Item> {
        //검색해서 나온 인덱스 리스트를 전시회 리스트에서 추출
        return indices.mapNotNull { index ->
            if (index in exhibitionList.indices) {
                exhibitionList[index]
            } else {
                null
            }
        }
    }
    private fun hideSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick() {
        dismiss()
    }
}
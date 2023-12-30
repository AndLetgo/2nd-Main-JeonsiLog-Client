package com.example.jeonsilog.view.search

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.databinding.FragmentSearchRecordBinding
import com.example.jeonsilog.widget.utils.GlideApp
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs


class RecordSearchFragment: BaseFragment<FragmentSearchRecordBinding>(R.layout.fragment_search_record) {
    private lateinit var adapter: RecordItemAdapter
    lateinit var itemList: ArrayList<String>
    //@@
    lateinit var viewModel: SearchViewModel
    override fun init() {
        //@@
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.searchData=viewModel
        binding.lifecycleOwner = this
        setEditBoxDeleteBt()
        loadSearchList()
        loadRandomList()
        setSearchList()
        setOnEditorActionListener()
        updateClearButtonVisibility(false)

        //텍스트 삭제 버튼
        binding.ivRecordDelete.setOnClickListener {
            binding.etSearchRecord.text.clear()
        }
    }
    private fun updateClearButtonVisibility(show: Boolean) {
        //텍스트 삭제버튼 visibility
        binding.ivRecordDelete.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun addItem(searchData: String) {
        adapter.add(searchData)
        //@@
        viewModel.updateItemList(itemList)

    }
    fun loadRandomList(){
        viewModel.randomExhibitionList = viewModel.exhibitionlist.shuffled().take(2)
        viewModel.setRandomList()
        val radiusDp = 8f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context?.resources?.displayMetrics).toInt()
        GlideApp.with(requireContext())
            .load(viewModel.unsplashUrl01)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(binding.ivRandom01)

        GlideApp.with(requireContext())
            .load(viewModel.unsplashUrl02)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(binding.ivRandom02)
    }
    fun loadSearchList(){
        itemList=prefs.getRecorList()

        viewModel.itemlist.observe(this, Observer { itemList ->
            if(itemList.size==0){
                viewModel.updateText("")
            }else{
                viewModel.updateText("최근검색어")
            }
        })
        if(itemList.size==0){
            viewModel.updateText("")
        }else{
            viewModel.updateText("최근검색어")
        }
    }
    fun setSearchList(){
        //최근검색어 리스트뷰의 아이템을 클릭시 SearchFragment의 replaceFragment()을 실행하기위해 frag 어댑터에 전달
        var frag=(parentFragment as? SearchFragment)
        adapter = RecordItemAdapter(requireContext(), R.layout.item_search_record, itemList,frag,viewModel)
        binding.lvSearch.adapter=adapter
    }
    fun setOnEditorActionListener(){
        //키보드 완료 버튼 눌럿을시 실행
        binding.etSearchRecord.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                val enteredText = binding.etSearchRecord.text.toString()
                if(enteredText.isBlank()){
                    Toast.makeText(context,"검색어를 입력하세요",Toast.LENGTH_SHORT).show()
                }else{
                    addItem(enteredText)
                    prefs.setRecorList(itemList)
                    //프래그먼트 전환 코드
                    (parentFragment as? SearchFragment)?.replaceFragment(SearchResultFrament(enteredText))

                    hideSoftKeyboard(requireActivity())
                    return@setOnEditorActionListener true
                }

            }
            false
        }
    }
    fun setEditBoxDeleteBt(){
        //edittext x 버튼 제어
        binding.etSearchRecord.addTextChangedListener(object : TextWatcher {
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
    }
    fun hideSoftKeyboard(activity: Activity) {
        //키보드 숨기기
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.etSearchRecord.setText("")
    }

}
package com.example.jeonsilog.view.search

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentSearchResultBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class SearchResultFragment(private var ediytextstr :String) : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    var  bottomNavigationView: BottomNavigationView?=null
    var initialTabPosition=0
    lateinit var viewModel: SearchViewModel
    val regexPattern = Regex("[!@#\\\$%^&*(),.?\\\":{}|<>;]")

    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(SearchResultFragment(ediytextstr))
                isRefresh.value = false
            }
        }
        loadSearchList()
        setbottomNavigation()
        setLayoutView()
        addTextChangedListener()    //EditText의 검색어가 변경될경우(EditTextdml x버튼 제어)
        setOnEditorActionListener() //EditText 검색기능 수행
        //x버튼
        binding.ivResultDelete.setOnClickListener {
            binding.etSearchResult.text.clear()
        }
    }

    fun loadSearchList(){
        viewModel.setItemlist(prefs.getRecorList())
    }
    fun setbottomNavigation(){
        val mActivity = context as MainActivity
        mActivity.setStateBn(false)
    }
    fun setLayoutView(){
        //뷰페이저설정(검색어)

        val pagerAdapter = SearchResultAdapter(childFragmentManager, lifecycle,ediytextstr)
        binding.vpResult.adapter=pagerAdapter
        binding.vpResult.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 페이지가 선택될 때 호출
                binding.tlResult.setScrollPosition(position, 0f, true)
            }
        })
        //EditText설정(검색어받아오기)
        binding.etSearchResult.setText(ediytextstr)
        //EditText설정(검색어가 비어있는 경우 X버튼 설정)
        if(ediytextstr==""){
            updateClearButtonVisibility(false)
        }else{
            updateClearButtonVisibility(true)
        }

        //탭 레이아웃 설정
        binding.tlResult.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    //tabLayoutPosition=it.position
                    // 선택된 탭의 위치에 따라 프래그먼트 전환
                    initialTabPosition=it.position
                    binding.vpResult.currentItem = it.position
                    // 첫 번째 탭이 선택되었을 때
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Not used in this example
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Not used in this example
            }
        })
    }
    private fun updateClearButtonVisibility(show: Boolean) {
        binding.ivResultDelete.visibility = if (show) View.VISIBLE else View.GONE
    }
    fun addTextChangedListener(){
        binding.etSearchResult.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 전 동작
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 동작 (실시간 감지)

                updateClearButtonVisibility(charSequence?.isNotEmpty() == true)

            }

            override fun afterTextChanged(editable: Editable?) {
                // 텍스트 변경 후 동작

            }
        })
    }
    fun setOnEditorActionListener(){
        binding.etSearchResult.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {

                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                var enteredText = binding.etSearchResult.text.toString()
                if(enteredText.isBlank()) {
                    Toast.makeText(context, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                }else if(regexPattern.containsMatchIn(enteredText)&&enteredText.length<=2){
                    Toast.makeText(context, "해당 검색어는 검색할수 없어요", Toast.LENGTH_SHORT).show()
                }else{
                    //=======================================================================================//
                    addItem(enteredText)

                    // 현재 선택된 탭의 위치를 기반으로 프래그먼트 생성
                    val currentTabPosition = binding.tlResult.selectedTabPosition

                    // 검색 결과를 반영하여 해당 탭의 자식 프래그먼트를 갱신
                    val pagerAdapter = SearchResultAdapter(childFragmentManager, lifecycle, enteredText)
                    binding.vpResult.adapter = pagerAdapter

                    binding.vpResult.currentItem = currentTabPosition
                    hideSoftKeyboard(requireActivity())
                    //=======================================================================================//
                }
                return@setOnEditorActionListener true
            }
            false
        }

    }
    private fun addItem(searchData: String) {
        //=======================================================================================//
        val itemListValue = viewModel.itemlist.value

        Log.d("내부테스트", "추가전 리스트: $itemListValue")
        val index = getIndexIfExists(itemListValue, searchData)
        if (index != -1) {
            Log.d("내부테스트", "기존 검색기록에 존재O")
            viewModel.removeItemAt(index)
        }else{
            Log.d("내부테스트", "기존 검색기록에 존재X")
        }
        //검색 개수제한
        Log.d("addBefore", "${viewModel.itemlist.value}: ")
        if (viewModel.itemlist.value?.size!! >=5){
            viewModel.removeItemAt(0)

        }
        //뷰모델 추가
        viewModel.addItem(searchData)
        Log.d("addAfter", "${viewModel.itemlist.value}: ")

        prefs.setRecorList(viewModel.itemlist.value!!)
        Log.d("내부테스트", "추가후 리스트: ${viewModel.itemlist.value}")

        //=======================================================================================//
    }
    fun getIndexIfExists(itemListValue: List<String>?, searchData: String): Int {
        return itemListValue?.indexOf(searchData) ?: -1
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus

        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

//    fun <T> MutableList<T>.prepend(element: T) {
//        add(0, element)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE

    }






}
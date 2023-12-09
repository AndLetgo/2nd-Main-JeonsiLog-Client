package com.example.jeonsilog.view.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.DataList.Companion.itemList
import com.example.jeonsilog.data.remote.dto.SearchData
import com.example.jeonsilog.databinding.FragmentSearchResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class SearchResultFrament(str :String) : Fragment()  {

    private var isViewCreated = true
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    var  bottomNavigationView: BottomNavigationView?=null
    var ediytextstr=str

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bnv_main)
        bottomNavigationView?.visibility = View.GONE
        val tabLayout = binding.TabLayout

        val pagerAdapter = SearchResultAdapter(childFragmentManager, lifecycle,ediytextstr)
        binding.MyViewPager.adapter=pagerAdapter

        binding.editTextSearchBox2.setText(ediytextstr)
        binding.editTextSearchBox2.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                val enteredText = binding.editTextSearchBox2.text.toString()
                if (itemList.size>=4){
                    itemList.removeAt(3)
                }
                itemList.prepend(SearchData(enteredText))
                val pagerAdapter = SearchResultAdapter(childFragmentManager, lifecycle,enteredText)
                //binding.MyViewPager.adapter=pagerAdapter
                hideSoftKeyboard(requireActivity())
                return@setOnEditorActionListener true
            }
            false
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    // 선택된 탭의 위치에 따라 프래그먼트 전환
                    binding.MyViewPager.currentItem = it.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Not used in this example
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Not used in this example
            }
        })
        val view = binding.root

        isViewCreated=true
        return view

    }


    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE
        _binding = null
    }
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus

        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
    fun <T> MutableList<T>.prepend(element: T) {
        add(0, element)
    }
}
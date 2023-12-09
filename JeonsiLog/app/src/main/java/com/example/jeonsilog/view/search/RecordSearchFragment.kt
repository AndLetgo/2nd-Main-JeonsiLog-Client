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
import com.example.jeonsilog.databinding.FragmentSearchRecordBinding


class RecordSearchFragment: Fragment() {
    private var _binding: FragmentSearchRecordBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecordItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentSearchRecordBinding.inflate(inflater, container, false)
        var p=(parentFragment as? SearchFragment)

        adapter = RecordItemAdapter(requireContext(), R.layout.item_search_record, itemList,p)

        binding.listView.adapter=adapter

        binding.editTextSearchBox1.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                val enteredText = binding.editTextSearchBox1.text.toString()
                addItem(SearchData(enteredText))


                //프래그먼트 전환 코드
                (parentFragment as? SearchFragment)?.replaceFragment(SearchResultFrament(enteredText))

                hideSoftKeyboard(requireActivity())
                return@setOnEditorActionListener true
            }
            false
        }

        val root: View = binding.root
        return root
    }


    private fun addItem(searchData: SearchData) {
        adapter.add(searchData)
    }
    override fun onPause() {
        super.onPause()
        binding.editTextSearchBox1.setText("")
    }
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus

        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}
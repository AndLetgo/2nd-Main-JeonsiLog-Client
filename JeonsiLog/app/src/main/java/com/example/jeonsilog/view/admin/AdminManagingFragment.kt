package com.example.jeonsilog.view.admin

import android.content.ClipData.Item
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.databinding.FragmentAdminManagingBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminManagingFragment : BaseFragment<FragmentAdminManagingBinding>(R.layout.fragment_admin_managing) {
    private lateinit var adminManagingRvAdapter : AdminManagingRvAdapter
    private lateinit var itemTouchHelper:ItemTouchHelper
    private var exhibitionList = mutableListOf<ExhibitionsInfo>()
    private var searchExhibitionList = mutableListOf<SearchInformationEntity>()

    override fun init() {
        setRecyclerView()

        binding.etSearchRecord.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchExhibition(binding.etSearchRecord.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun setRecyclerView(){
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(), 0)
            if(response.isSuccessful && response.body()!!.check){
                for(i in 0..9){
                    exhibitionList.add(i,response.body()!!.information.data[i])
                }
            }
        }
        adminManagingRvAdapter = AdminManagingRvAdapter(exhibitionList, requireContext())
        binding.rvExhibitionList.adapter = adminManagingRvAdapter
        binding.rvExhibitionList.layoutManager = LinearLayoutManager(requireContext())

        val touchHelperCallback = AdminManagingTouchHelperCallback(adminManagingRvAdapter)
        itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvExhibitionList)
    }

    private fun searchExhibition(searchWord:String){
        Log.d("managing", "searchExhibition: searchWord: $searchWord")
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),searchWord,0)
            if(response.isSuccessful && response.body()!!.check){
                Log.d("managing", "searchExhibition: successs")
                searchExhibitionList.addAll(response.body()!!.information.data)
                Log.d("managing", "searchExhibition: searchExhibitionList.size: ${searchExhibitionList.size}")
            }
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.item_admin_managing_dropdown, searchExhibitionList)
        binding.etSearchRecord.setAdapter(adapter)
        binding.etSearchRecord.dropDownHeight = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}
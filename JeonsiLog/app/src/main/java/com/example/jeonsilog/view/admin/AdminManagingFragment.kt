package com.example.jeonsilog.view.admin

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsPlaceEntity
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionSequenceRequest
import com.example.jeonsilog.data.remote.dto.exhibition.SearchByNameEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.UpdateSequenceInfo
import com.example.jeonsilog.databinding.FragmentAdminManagingBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.math.log

class AdminManagingFragment : BaseFragment<FragmentAdminManagingBinding>(R.layout.fragment_admin_managing) {
    private lateinit var adminManagingRvAdapter : AdminManagingRvAdapter
    private lateinit var itemTouchHelper:ItemTouchHelper
    private var exhibitionList = mutableListOf<SearchByNameEntity>()
    private var exhibitionNameList = mutableListOf<String>()
    private var searchExhibitionList = mutableListOf<SearchByNameEntity>()
    private val adminViewModel : AdminViewModel by activityViewModels()
    private var checkFailure = false
    private var selectedExhibitionName = ""
    val TAG = "managing"
    override fun init() {
        adminViewModel.setCheckListCount(false)
        setRecyclerView()

        binding.vm = adminViewModel
        binding.lifecycleOwner = this
        binding.etSearchRecord.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.isNotEmpty()){
                    searchExhibition(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSaveSequence.setOnClickListener {
            if(adminViewModel.checkListCount.value!!){
                saveExhibitionSequence()
            }else{
                Toast.makeText(requireContext(), getString(R.string.toast_managing_requre_full), Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearchRecord.setOnItemClickListener { parent, view, position, id ->
            var searchedItem = SearchByNameEntity(0,"")

            selectedExhibitionName = binding.etSearchRecord.text.toString()
            binding.etSearchRecord.text = null
            for(i in searchExhibitionList){
                if(i.exhibitionName == selectedExhibitionName){
                    searchedItem = i
                }
            }

            for(i in exhibitionList){
                if(searchedItem.exhibitionId == i.exhibitionId){
                    Toast.makeText(requireContext(), getString(R.string.toast_managing_duplication), Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                }
            }
            if(exhibitionList.size == 10){
                Toast.makeText(requireContext(), getString(R.string.toast_managing_full), Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            exhibitionList.add(searchedItem)
            if(exhibitionList.size == 10){
                adminViewModel.setCheckListCount(true)
            }
            binding.rvExhibitionList.adapter!!.notifyItemInserted(exhibitionList.size-1)
        }
    }

    private fun setRecyclerView(){
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(), 0)
            if(response.isSuccessful && response.body()!!.check){
                val data =response.body()!!.information.data
                for(i in 0..9){
                    val item = SearchByNameEntity(data[i].exhibitionId, data[i].exhibitionName)
                    exhibitionList.add(i,item)
                }
            }
        }
        adminManagingRvAdapter = AdminManagingRvAdapter(exhibitionList, requireContext())
        binding.rvExhibitionList.adapter = adminManagingRvAdapter
        binding.rvExhibitionList.layoutManager = LinearLayoutManager(requireContext())

        adminManagingRvAdapter.setOnItemClickListener(object : AdminManagingRvAdapter.OnItemClickListener{
            override fun deleteItem(position: Int) {
                exhibitionList.removeAt(position)
                adminManagingRvAdapter.notifyDataSetChanged()
                adminViewModel.setCheckListCount(false)
            }

            override fun checkItemMoved() {
                adminViewModel.setCheckListCount(true)
            }

        })

        val touchHelperCallback = AdminManagingTouchHelperCallback(adminManagingRvAdapter)
        itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvExhibitionList)
    }

    private fun searchExhibition(searchWord:String){
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().searchExhibitionByName(encryptedPrefs.getAT(),searchWord,0)
            if(response.isSuccessful && response.body()!!.check){
                searchExhibitionList = response.body()!!.information.data.toMutableList()
                if(searchExhibitionList.size ==1){
                    checkFailure=true
                }
            }
        }

        exhibitionNameList = mutableListOf()
        for(i in searchExhibitionList){
            exhibitionNameList.add(i.exhibitionName)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.item_admin_managing_dropdown, exhibitionNameList)
        binding.etSearchRecord.setAdapter(adapter)
        binding.etSearchRecord.dropDownHeight = resources.getDimensionPixelSize(R.dimen.dropdown_height)
    }

    private fun saveExhibitionSequence(){
        val updateList = mutableListOf<UpdateSequenceInfo>()
        for (i in 0..9){
            val item = UpdateSequenceInfo(
                i+1,
                exhibitionList[i].exhibitionId
            )
            updateList.add(item)
        }
        val body = PatchExhibitionSequenceRequest(updateList)
        var isSuccess = false
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().patchExhibitionSequence(encryptedPrefs.getAT(),body)
            if(response.isSuccessful && response.body()!!.check){
                isSuccess = true
            }
        }
        if(isSuccess){
            Toast.makeText(requireContext(), getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show()
            adminViewModel.setCheckListCount(false)
        }else{
            Toast.makeText(requireContext(), getString(R.string.toast_save_failure), Toast.LENGTH_SHORT).show()
        }
    }
}
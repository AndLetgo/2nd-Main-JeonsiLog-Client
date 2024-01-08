package com.example.jeonsilog.view.notification

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.alarm.AlarmInformation
import com.example.jeonsilog.databinding.FragmentNotiExhibitionBinding
import com.example.jeonsilog.repository.alarm.AlarmRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotiExhibitionFragment : BaseFragment<FragmentNotiExhibitionBinding>(R.layout.fragment_noti_exhibition) {
    private val notiList = mutableListOf<AlarmInformation>()

    override fun init() {
        getAlram()
        val adapter = NotificationRvAdapter(notiList, requireContext())
        binding.rvNotiExhibition.adapter = adapter
        binding.rvNotiExhibition.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getAlram(){
        runBlocking(Dispatchers.IO){
            val response = AlarmRepositoryImpl().getExhibitionAlarm(GlobalApplication.encryptedPrefs.getAT())

            if(response.isSuccessful && response.body()!!.check){
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    notiList.add(data.next())
                }
            }
        }
    }
}
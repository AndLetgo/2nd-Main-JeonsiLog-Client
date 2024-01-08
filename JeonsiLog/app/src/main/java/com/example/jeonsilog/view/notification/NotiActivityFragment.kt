package com.example.jeonsilog.view.notification

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.alarm.AlarmInformation
import com.example.jeonsilog.databinding.FragmentNotiActivityBinding
import com.example.jeonsilog.repository.alarm.AlarmRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotiActivityFragment : BaseFragment<FragmentNotiActivityBinding>(R.layout.fragment_noti_activity) {
    private val notiList = mutableListOf<AlarmInformation>()

    override fun init() {
        getAlram()
        val adapter = NotificationRvAdapter(notiList, requireContext())
        binding.rvNotiActivity.adapter = adapter
        binding.rvNotiActivity.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getAlram(){
        runBlocking(Dispatchers.IO){
            val response = AlarmRepositoryImpl().getActivityAlarm(encryptedPrefs.getAT())

            if(response.isSuccessful && response.body()!!.check){
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    notiList.add(data.next())
                }
            }
        }
    }
}

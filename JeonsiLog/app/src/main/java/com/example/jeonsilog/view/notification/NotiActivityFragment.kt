package com.example.jeonsilog.view.notification

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.alarm.AlarmEntity
import com.example.jeonsilog.databinding.FragmentNotiActivityBinding
import com.example.jeonsilog.repository.alarm.AlarmRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotiActivityFragment : BaseFragment<FragmentNotiActivityBinding>(R.layout.fragment_noti_activity) {
    private val notiList = mutableListOf<AlarmEntity>()
    private var page = 0
    private var newItemCount = 0
    private var isFinished = false

    override fun init() {
        getAlarm()

        val adapter = NotificationRvAdapter(notiList, requireContext())
        binding.rvNotiActivity.adapter = adapter
        binding.rvNotiActivity.layoutManager = LinearLayoutManager(requireContext())

        binding.rvNotiActivity.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                if(totalCount == rvPosition){
                    if(!isFinished){
                        getAlarm()

                        recyclerView.post {
                            adapter.notifyItemRangeInserted(totalCount+1, newItemCount)
                            newItemCount = 0
                        }
                    }
                }
            }
        })
    }

    private fun getAlarm(){
        runBlocking(Dispatchers.IO){
            val response = AlarmRepositoryImpl().getActivityAlarm(encryptedPrefs.getAT(), page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.data.size
                val data = response.body()!!.information.data.listIterator()
                while (data.hasNext()){
                    notiList.add(data.next())
                }
            } else {
                isFinished = true
            }

            page++
        }
    }
}
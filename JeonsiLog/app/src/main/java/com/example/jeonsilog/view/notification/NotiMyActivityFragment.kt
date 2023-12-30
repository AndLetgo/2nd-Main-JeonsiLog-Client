package com.example.jeonsilog.view.notification

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentNotiMyactivityBinding

class NotiMyActivityFragment : BaseFragment<FragmentNotiMyactivityBinding>(R.layout.fragment_noti_myactivity) {
    override fun init() {
        val notiList = mutableListOf<NotificationModel>()
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "[생의 찬미]", content = "전시 시작까지 3일 남았어요", profileImg = "https://picsum.photos/id/10/200/300", time = "지금", type = 2))
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "[Little Forest Season2]", content = "전시 시작까지 3일 남았어요", profileImg = "https://picsum.photos/id/11/200/300", time = "지금", type = 2))
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "[최은정: Monument Valley]", content = "전시 시작까지 3일 남았어요", profileImg = "https://picsum.photos/id/12/200/300", time = "지금", type = 2))

        val adapter = NotificationRvAdapter(notiList)
        binding.rvNotiMyactivity.adapter = adapter
        binding.rvNotiMyactivity.layoutManager = LinearLayoutManager(requireContext())
    }
}
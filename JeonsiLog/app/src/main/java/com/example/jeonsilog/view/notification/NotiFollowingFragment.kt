package com.example.jeonsilog.view.notification

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentNotiFollowingBinding

class NotiFollowingFragment : BaseFragment<FragmentNotiFollowingBinding>(R.layout.fragment_noti_following) {

    override fun init() {
        val notiList = mutableListOf<NotificationModel>()
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "안드레고", content = "님이 감상평을 남겼어요", profileImg = "https://picsum.photos/id/1/200/300", time = "지금", type = 0))
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "안드레고", content = "님이 감상평을 남겼어요", profileImg = "https://picsum.photos/id/2/200/300", time = "지금", type = 0))
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "안드레고", content = "님이 나를 팔로우해요", profileImg = "https://picsum.photos/id/3/200/300", time = "지금", type = 1))
        notiList.add(NotificationModel(userId = 1, exhibitionId = 1, title = "안드레고", content = "님이 별점을 남겼어요", profileImg = "https://picsum.photos/id/4/200/300", time = "지금", type = 0))
        val adapter = NotificationRvAdapter(notiList)
        binding.rvNotiFollowing.adapter = adapter
        binding.rvNotiFollowing.layoutManager = LinearLayoutManager(requireContext())
    }

}
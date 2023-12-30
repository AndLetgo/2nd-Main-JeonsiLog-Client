package com.example.jeonsilog.view.otheruser

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserRatingBinding
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.SpannableStringUtil

class OtherUserRatingFragment(private val vm: OtherUserViewModel): BaseFragment<FragmentOtherUserRatingBinding>(R.layout.fragment_other_user_rating) {
    override fun init() {
        val list = mutableListOf<OtherUserRatingModel>()
        list.add(OtherUserRatingModel(1, "[CONNECT]", 2.5f))
        list.add(OtherUserRatingModel(2, "[안드레고]", 4f))
        list.add(OtherUserRatingModel(3, "[전시로그]", 4.5f))
        list.add(OtherUserRatingModel(4, "[안드로이드]", 5f))
        list.add(OtherUserRatingModel(1, "[CONNECT]", 2.5f))
        list.add(OtherUserRatingModel(2, "[안드레고]", 4f))
        list.add(OtherUserRatingModel(3, "[전시로그]", 4.5f))
        list.add(OtherUserRatingModel(4, "[안드로이드]", 5f))


        if(list.isEmpty()){
            binding.rvOtherUserRating.visibility = View.GONE
            binding.tvOtherUserRatingCount.visibility = View.GONE
            binding.ivOtherUserRatingEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserRatingEmptyTitle.visibility = View.VISIBLE
        } else {
            val adapter = OtherUserRvAdapter<OtherUserRatingModel>(list, 0)
            binding.rvOtherUserRating.adapter = adapter
            binding.rvOtherUserRating.layoutManager = LinearLayoutManager(requireContext())
            binding.rvOtherUserRating.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

            binding.tvOtherUserRatingCount.text = SpannableStringUtil().highlightNumber(getString(R.string.other_my_rating_count, vm.nick.value ,list.size), requireContext())
        }
    }
}
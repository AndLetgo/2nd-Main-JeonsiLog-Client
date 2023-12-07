package com.example.jeonsilog.view

import android.view.View
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityMainBinding
import com.example.jeonsilog.view.home.HomeFragment

class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {

    override fun init() {
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()
                }
                R.id.item_search->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,SearchFragment()).setReorderingAllowed(true).commitAllowingStateLoss()
                }
                R.id.item_photoCalendar->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,PhotoCalendarFragment()).commit()
                }
                R.id.item_notification->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,NotificationFragment()).commit()
                }
                R.id.item_mypage->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,MyPageFragment()).commit()
                }
            }
            true
        }

    }

    fun setStateBn(isVisible:Boolean){
        if(isVisible){
            binding.bnvMain.visibility = View.VISIBLE
        }else{
            binding.bnvMain.visibility = View.GONE
        }
        //사용 시 해당 프레그먼트에서 아래처럼 사용하면 됨 (확인 후 이 부분은 지우셔도 됩니다)
//        val mainActivity = activity as MainActivity
//        mainActivity.setStateBn(false)
    }
}
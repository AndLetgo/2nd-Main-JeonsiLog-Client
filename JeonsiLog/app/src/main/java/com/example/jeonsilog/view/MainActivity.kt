package com.example.jeonsilog.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {

    override fun init() {
        supportFragmentManager.beginTransaction().replace(R.id.fl_main,HomeFragment()).commit()

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,HomeFragment()).commit()
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
}
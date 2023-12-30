package com.example.jeonsilog.view

import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityMainBinding
import com.example.jeonsilog.view.admin.AdminManagingFragment
import com.example.jeonsilog.view.admin.AdminReportFragment
import com.example.jeonsilog.view.home.HomeFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {

    override fun init() {
//        supportFragmentManager.beginTransaction().replace(R.id.fl_main, HomeFragment()).commit()
        //admin인지 판단하는 로직 추가
        binding.bnvMain.isVisible = false
        binding.bnvAdmin.isVisible = true

        //Main Bottom Nav
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

        //Admin Bottom Nav
        binding.bnvAdmin.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_admin_home->{
                    setStateFl("home")
                    val navController = findNavController(R.id.fcv_nav_admin)
                    navController.navigate(R.id.homeFragment)
                }
                R.id.item_admin_search->{
                    setStateFl("")
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,SearchFragment()).setReorderingAllowed(true).commitAllowingStateLoss()
                }
                R.id.item_admin_report->{
                    setStateFl("")
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,AdminReportFragment()).commit()
                }
                R.id.item_admin_managing->{
                    setStateFl("")
                    supportFragmentManager.beginTransaction().replace(R.id.fl_main,AdminManagingFragment()).commit()
                }
            }
            true
        }

    }

    fun setStateBn(isVisible:Boolean, type:String){
        var view = binding.bnvMain
        when(type){
            "main" -> view = binding.bnvMain
            "admin" -> view = binding.bnvAdmin
        }
        if(isVisible){
            view.visibility = View.VISIBLE
        }else{
            view.visibility = View.GONE
        }
    }
    fun setStateFl(type:String){
        when(type){
            "home" -> {
                binding.flMain.visibility = View.GONE
                binding.fcvNavAdmin.visibility = View.VISIBLE
            }
            else -> {
                binding.flMain.visibility = View.VISIBLE
                binding.fcvNavAdmin.visibility = View.GONE
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
package com.example.jeonsilog.view.exhibition

import androidx.fragment.app.Fragment
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityExtraBinding
import com.example.jeonsilog.view.home.HomeFragment
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class ExtraActivity : BaseActivity<ActivityExtraBinding>({ ActivityExtraBinding.inflate(it)}) {
    override fun init() {
        when(extraActivityReference){
            0 -> supportFragmentManager.beginTransaction().replace(R.id.fl_extra, ExhibitionFragment()).commit()
        }

    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fl_extra, fragment).commit()
    }
}
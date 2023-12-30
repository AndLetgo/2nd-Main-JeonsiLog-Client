package com.example.jeonsilog.view.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding>(
    R.layout.fragment_search) {
    override fun init() {
        replaceFragment(RecordSearchFragment())
    }


    fun replaceFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_search, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
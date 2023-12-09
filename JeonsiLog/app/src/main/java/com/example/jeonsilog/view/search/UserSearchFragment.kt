package com.example.jeonsilog.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.databinding.FragmentUserSearchBinding



class UserSearchFragment : Fragment() {
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        binding.UserSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val items = listOf(
            UserSearchItem("전시회 1", ),
            UserSearchItem("전시회 2", ),
            UserSearchItem("전시회 3", ),
            // Add more items as needed
        )

        val adapter = UserSearchItemAdapter(items)
        binding.UserSearchRecyclerView.adapter = adapter

        val root: View = binding.root
        return root
    }
}
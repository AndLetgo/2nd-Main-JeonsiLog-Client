package com.example.jeonsilog.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding


class ExhibitionPlaceFragment : Fragment() {
    private var _binding: FragmentExhibitionPlaceBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExhibitionPlaceBinding.inflate(inflater, container, false)
        binding.ExhibitionPlaceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val items = listOf(
            ExhibitionPlaceItem("전시회 1", ),
            ExhibitionPlaceItem("전시회 2", ),
            // Add more items as needed
        )

        val adapter = ExhibitionPlaceItemAdapter(items)
        binding.ExhibitionPlaceRecyclerView.adapter = adapter

        val root: View = binding.root
        return root
    }
}
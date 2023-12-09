package com.example.jeonsilog.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.databinding.FragmentExihibitionInfoBinding

class ExhibitionInfoFragment(itemlist: List<ExhibitionInfoItem>) : Fragment() {
    private var _binding: FragmentExihibitionInfoBinding? = null
    private val binding get() = _binding!!
    val items=itemlist
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExihibitionInfoBinding.inflate(inflater, container, false)
        binding.ExhibitionInfoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ExhibitionInfoItemAdapter(items)
        binding.ExhibitionInfoRecyclerView.adapter = adapter

        val root: View = binding.root
        return root
    }
}
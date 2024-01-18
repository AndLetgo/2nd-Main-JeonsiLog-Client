package com.example.jeonsilog.view.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jeonsilog.R
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs


class RecordItemAdapter(
    context: Context, resource: Int, objects: List<String>,
    private var viewModel: SearchViewModel,private val callback: AdapterCallback) :
    ArrayAdapter<String>(context, resource, objects) {

    private val mContext: Context = context
    private val mResource: Int = resource


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(mResource, parent, false)

        val itemTextView: TextView = view.findViewById(R.id.SearchTx)
        val itemView: ConstraintLayout = view.findViewById(R.id.item_search_record)
        val deleteButton: ImageView = view.findViewById(R.id.deleteBt)

        val currentItem: String = getItem(position).toString()
        viewModel.setItemlist(prefs.getRecorList())
        itemTextView.text = currentItem
        itemView.setOnClickListener {
            //1.검색기록 클릭처리
            (context as MainActivity).moveSearchResultFrament(currentItem)
        }
        deleteButton.setOnClickListener {

            val listIndex=viewModel.itemlist.value?.indexOf(currentItem)
            viewModel.removeItemAt(listIndex!!)
            prefs.setRecorList(viewModel.itemlist.value!!)
            callback.onAdapterItemClicked()
        }

        return view
    }
    interface AdapterCallback{
        fun onAdapterItemClicked()
    }

}




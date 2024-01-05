package com.example.jeonsilog.view.search

import android.content.Context
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
    private var frag : SearchFragment?=null,
    private var viewModel: SearchViewModel
) :
    ArrayAdapter<String>(context, resource, objects) {

    private val mContext: Context = context
    private val mResource: Int = resource
    private val MAX_ITEMS = 4

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(mResource, parent, false)

        val itemTextView: TextView = view.findViewById(R.id.SearchTx)
        val itemVㅑew: ConstraintLayout = view.findViewById(R.id.item_search_record)
        val deleteButton: ImageView = view.findViewById(R.id.deleteBt)

        val currentItem: String = getItem(position).toString()
        var itemList= prefs.getRecorList()
        //@@
        itemTextView.text = currentItem
        itemVㅑew.setOnClickListener {
            (context as MainActivity).moveSearchResultFrament(currentItem)
        }
        deleteButton.setOnClickListener {
            remove(currentItem)
            notifyDataSetChanged()
            itemList.removeAt(position)
            viewModel.updateItemList(itemList)
            prefs.setRecorList(itemList)
        }

        return view
    }
    override fun add(item: String?) {
        if (item != null) {
            if (count >= MAX_ITEMS) {
                remove(getItem(count - 1))
            }
            super.insert(item, 0)
        }
    }


}




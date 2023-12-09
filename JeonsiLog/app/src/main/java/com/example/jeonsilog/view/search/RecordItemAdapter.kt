package com.example.jeonsilog.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.SearchData



class RecordItemAdapter(
    context: Context, resource: Int, objects: List<SearchData>,
    private var frag : SearchFragment?=null
) :
    ArrayAdapter<SearchData>(context, resource, objects) {

    private val mContext: Context = context
    private val mResource: Int = resource
    private val MAX_ITEMS = 4


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(mResource, parent, false)

        val itemTextView: TextView = view.findViewById(R.id.SearchTx)
        val deleteButton: Button = view.findViewById(R.id.deleteBt)

        val currentItem: SearchData = getItem(position) ?: SearchData("")

        itemTextView.text = currentItem.SearchStr
        itemTextView.setOnClickListener {
            //frag?.replaceFragment(SearchResultFrament(currentItem.SearchStr))
        }
        deleteButton.setOnClickListener {
            remove(currentItem)
            notifyDataSetChanged()
        }

        return view
    }
    override fun add(item: SearchData?) {
        if (item != null) {
            if (count >= MAX_ITEMS) {
                remove(getItem(count - 1))
            }
            super.insert(item, 0)
        }
    }


}




package com.example.jeonsilog.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem

class ExhibitionInfoItemAdapter(private val items: List<ExhibitionInfoItem>) : RecyclerView.Adapter<ExhibitionInfoItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exhibitionnameTextView: TextView = view.findViewById(R.id.exhibitionnameTx)
        val exhibitionlocationTextView: TextView = view.findViewById(R.id.exhibitionlocationTx)
        val exhibitionplaceTextView: TextView = view.findViewById(R.id.exhibitionplaceTx)
        val exhibitiondateTextView: TextView = view.findViewById(R.id.exhibitiondateTx)
        val exhibitionpriceTextView: TextView = view.findViewById(R.id.exhibitionpriceTx)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exhibition_layout_test, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.exhibitionnameTextView.text = item.exhibitionname
        holder.exhibitionlocationTextView.text = item.exhibitionlocation
        holder.exhibitionplaceTextView.text = item.exhibitionplace
        holder.exhibitiondateTextView.text = item.exhibitiondate
        holder.exhibitionpriceTextView.text = item.exhibitionplace


    }

    override fun getItemCount(): Int {
        return items.size
    }
}

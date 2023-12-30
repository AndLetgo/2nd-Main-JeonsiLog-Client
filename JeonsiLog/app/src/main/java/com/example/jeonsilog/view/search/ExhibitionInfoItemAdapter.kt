package com.example.jeonsilog.view.search

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem

class ExhibitionInfoItemAdapter(private val context: Context, private val items: List<ExhibitionInfoItem>) : RecyclerView.Adapter<ExhibitionInfoItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exhibitionnameTextView: TextView = view.findViewById(R.id.tv_title)
        val exhibitionlocationTextView: TextView = view.findViewById(R.id.tv_address)
        val exhibitionplaceTextView: TextView = view.findViewById(R.id.tv_place)
        val exhibitiondateTextView: TextView = view.findViewById(R.id.tv_keyword_operating)
        val exhibitionpriceTextView: TextView = view.findViewById(R.id.tv_keyword_price)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_exhibition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val radiusDp = 4f
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusDp, context.resources.displayMetrics).toInt()

        val unsplashUrl = "https://picsum.photos/id/${position+50}/200/300"
        Glide.with(context)
            .load(unsplashUrl)
            .transform(CenterCrop(), RoundedCorners(radiusPx))
            .into(holder.itemView.findViewById(R.id.iv_poster))

        holder.exhibitionnameTextView.text = item.exhibitionName
        holder.exhibitionlocationTextView.text = item.exhibitionLocation
        holder.exhibitionplaceTextView.text = item.exhibitionPlace
        if (item.exhibitionPrice==null){
            holder.exhibitiondateTextView.isGone=true
        }else{
            holder.exhibitiondateTextView.text = item.exhibitionPrice
        }
        if (item.exhibitionDate==null){
            holder.exhibitionpriceTextView.isGone=true
        }else{
            holder.exhibitionpriceTextView.text = item.exhibitionDate
        }



    }

    override fun getItemCount(): Int {
        return items.size
    }
}

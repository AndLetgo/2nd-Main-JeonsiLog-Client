package com.example.jeonsilog.view.admin

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.report.GetReportsInformation
import com.example.jeonsilog.databinding.ItemAdminReportBinding

class AdminReportRvAdapter(
    private val reportList:MutableList<GetReportsInformation>, private val context: Context):
    RecyclerView.Adapter<AdminReportRvAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemAdminReportBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int){
            val item = reportList[position]
            if (item.isChecked) {
                itemView.alpha = 0.4f
            } else {
                itemView.alpha = 1.0f
            }

            var icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_user)
            var content = ""
            var nameIndex = item.name.length+2
            val boldSpan = StyleSpan(Typeface.BOLD)
            when(item.reportType){
                "EXHIBITION" -> {
                    icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_poster)!!
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_poster_content),
                        item.name
                    )
                    binding.tvCountBadge.visibility = ViewGroup.GONE
                }
                "REVIEW" -> {
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_review_content),
                        item.name
                    )
                    binding.tvCountBadge.visibility = ViewGroup.GONE
                }
                "REPLY" -> {
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_reply_content),
                        item.name
                    )
                    binding.tvCountBadge.visibility = ViewGroup.GONE
                }
                "ADDRESS" -> {
                    icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_address)!!
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_address_content),
                        item.name
                    )
                }
                "PHONE_NUMBER" -> {
                    icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_phone)!!
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_phone_content),
                        item.name
                    )
                }
                "LINK" -> {
                    icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_link)!!
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_link_content),
                        item.name
                    )
                }
            }
            Glide.with(context)
                .load(icDrawable)
                .into(binding.ivIcon)

            val spannableString = SpannableString(content)
            spannableString.setSpan(boldSpan, 0, nameIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvReportContent.text = spannableString

            val count = item.counting
            if(count<1){
                binding.tvCountBadge.visibility = ViewGroup.GONE
            }else{
                if(count>9){
                    binding.tvCountBadge.text = context.getString(R.string.admin_report_count_over_nine)
                }else{
                    binding.tvCountBadge.text = count.toString()
                }
                binding.tvCountBadge.backgroundTintList = badgeColor(count)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reportList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        if(position != RecyclerView.NO_POSITION){
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, reportList[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: GetReportsInformation, position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private fun badgeColor(count:Int):ColorStateList{
        val color:Int
        if(count<=4){
            color =  context.getColor(R.color.badge_blue)
        }else if(count<=8){
            color =  context.getColor(R.color.badge_yellow)
        }else{
            color = context.getColor(R.color.badge_red)
        }
        return ColorStateList.valueOf(color)
    }
}
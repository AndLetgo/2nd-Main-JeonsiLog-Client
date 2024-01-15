package com.example.jeonsilog.view.admin

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
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
            var icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_user)
            var content = ""
            var nameIndex = item.name.length
            val boldSpan = StyleSpan(Typeface.BOLD)
            when(item.reportType){
                "EXHIBITION" -> {
                    icDrawable = ContextCompat.getDrawable(context, R.drawable.ic_admin_report_poster)!!
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_poster_content),
                        item.name
                    )
                }
                "REVIEW" -> {
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_review_content),
                        item.name
                    )
                }
                "REPLY" -> {
                    content = String.format(
                        ContextCompat.getString(context, R.string.admin_report_reply_content),
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
}
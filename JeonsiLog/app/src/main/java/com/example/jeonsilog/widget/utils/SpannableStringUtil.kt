package com.example.jeonsilog.widget.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.jeonsilog.R

class SpannableStringUtil {
    fun highlightNumber(str: String, context: Context): SpannableString {
        val spannableStr = SpannableString(str)

        val numberPattern = "\\d+".toRegex()
        val matchResults = numberPattern.findAll(str)

        for (matchResult in matchResults) {
            val start = matchResult.range.first
            val end = matchResult.range.last + 1

            spannableStr.setSpan(
                ForegroundColorSpan(context.getColor(R.color.basic_point)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableStr
    }
}
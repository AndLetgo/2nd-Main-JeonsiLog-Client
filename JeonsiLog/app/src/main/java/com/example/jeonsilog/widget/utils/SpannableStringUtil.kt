package com.example.jeonsilog.widget.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
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

    fun boldTextBetweenBrackets(str: String): SpannableString {
        val startBracket = '['
        val endBracket = ']'

        val startIndex = str.indexOf(startBracket)
        val endIndex = str.indexOf(endBracket)

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            val spannableString = SpannableString(str)

            // Bold 스타일 적용
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            Log.d("Span", "성공")
            return spannableString
        } else {
            // 대괄호가 없거나 시작 대괄호가 끝 대괄호보다 뒤에 있는 경우
            Log.d("Span", "실패")
            return SpannableString(str)
        }
    }

}
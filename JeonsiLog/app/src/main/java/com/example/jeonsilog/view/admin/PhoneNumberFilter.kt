package com.example.jeonsilog.view.admin

import android.text.InputFilter
import android.text.Spanned

class PhoneNumberFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // 원하는 패턴으로 수정
        val pattern = "[0-9-]+".toRegex()

        if (source != null && !pattern.matches(source)) {
            return ""
        }

        return null
    }
}
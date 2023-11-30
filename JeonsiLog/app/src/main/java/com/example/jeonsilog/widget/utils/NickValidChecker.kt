package com.example.jeonsilog.widget.utils

import java.util.regex.Pattern

class NickValidChecker {
    fun isLengthValid(input: String): Boolean {
        return input.length >= 2
    }

    fun hasProhibitedWord(input: String): Boolean {

        return false
    }

    fun hasSpecialCharacter(input: String): Boolean {
        val regex = "[!@#\$%^&*(),.?\":{}|<> ]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(input).find()
    }
}
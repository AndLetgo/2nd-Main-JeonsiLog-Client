package com.example.jeonsilog.widget.utils

import java.util.regex.Pattern

class NickValidChecker {
    fun isLengthValid(input: String): Boolean {
        return input.length >= 2
    }

    fun hasSpecialCharacter(input: String): Boolean {
        val regex = "[!@#\$%^&*()_+={}\\[\\]:;<>,.?/~|\\\\\\\"'\\s-]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(input).find()
    }

    fun isNotPair(input: String): Boolean{
        val regex = "[ㅏ-ㅣㄱ-ㅎ]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(input).find()
    }

    fun allCheck(input: String): Boolean{
        if(isLengthValid(input) && !hasSpecialCharacter(input) && !isNotPair(input)){
            return true
        }
        return false
    }
}
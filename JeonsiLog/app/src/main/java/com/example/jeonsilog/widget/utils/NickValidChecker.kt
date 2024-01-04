package com.example.jeonsilog.widget.utils

import java.util.regex.Pattern

class NickValidChecker {
    private val prohibitedWords: List<String> = listOf("전시로그", "안드레고")

    fun isLengthValid(input: String): Boolean {
        return input.length >= 2
    }

    fun hasProhibitedWord(input: String): Boolean {
        for (str in prohibitedWords) {
            if (input.contains(str)) {
                return true
            }
        }
        return false
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
        if(isLengthValid(input) && !hasProhibitedWord(input) && !hasSpecialCharacter(input) && !isNotPair(input)){
            return true
        }
        return false
    }
}
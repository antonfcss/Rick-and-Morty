package com.example.rickmorty.base

fun String.extractLastPartToInt(): Int {
    return this.substringAfterLast('/').toInt()
}

fun String.extractLastPartToIntOrZero(): Int {
    return this.substringAfterLast('/').toIntOrNull() ?: 0
}
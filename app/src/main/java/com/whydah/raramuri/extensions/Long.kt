package com.whydah.raramuri.extensions

fun Long.toSecond(): Long {
    return this / 1000
}

fun Long.toMilliseconds(): Long {
    return this * 1000
}
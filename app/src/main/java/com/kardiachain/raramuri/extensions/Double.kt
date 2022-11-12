package com.kardiachain.raramuri.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double.formatThousandWithPostFix(
    maxDigit: Int = 2,
    postFix: String = "km"
): String {
    val nf = NumberFormat.getInstance(Locale.US)
    nf.maximumFractionDigits = maxDigit
    return "${nf.format(this / 1000)} $postFix"
}
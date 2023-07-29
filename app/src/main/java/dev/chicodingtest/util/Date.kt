package dev.chicodingtest.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToStandardString(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}
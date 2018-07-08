package com.shelvz.assignment.utilities

import com.shelvz.assignment.network.NetworkConstants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String?.toDate(format: String = NetworkConstants.API_STRING_DATE_FULL_FORMAT_2): Date? {
    try {
        val sdf = SimpleDateFormat(format)
        return sdf.parse(this)
    } catch (exception: ParseException) {
        exception.printStackTrace()
    }
    return null
}


fun Date?.toString(format: String = NetworkConstants.API_STRING_DATE_FULL_FORMAT_2): String? {
    val sdf = SimpleDateFormat(format)
    return sdf.format(this)
}

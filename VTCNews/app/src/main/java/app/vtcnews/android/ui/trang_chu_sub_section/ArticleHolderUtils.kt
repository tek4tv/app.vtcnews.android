package app.vtcnews.android.ui.trang_chu_sub_section

import android.content.res.Resources
import app.vtcnews.android.R
import app.vtcnews.android.model.Article
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun getDateDiff(date: String, resource: Resources): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val date = dateFormatter.parse(date)
    val diff = abs(Date().time - date!!.time)
    val diffMinutes: Long = diff / (60 * 1000)
    val diffHours: Long = diff / (60 * 60 * 1000)
    val diffDays: Long = diff / (60 * 60 * 1000 * 24)
    val diffMonths = (diff / (60 * 60 * 1000 * 24 * 30.41666666))
    val diffYears: Long = diff / (60.toLong() * 60 * 1000 * 24 * 365)


    return when {
        diffYears >= 1 -> resource.getString(R.string.years_before, diffYears.toInt())
        diffMonths >= 1 -> resource.getString(R.string.months_before, diffMonths.toInt())
        diffDays >= 1 -> resource.getString(R.string.days_before, diffDays.toInt())
        diffHours >= 1 -> resource.getString(R.string.hours_before, diffHours.toInt())
        diffMinutes >= 1 -> resource.getString(R.string.minutes_before, diffMinutes.toInt())
        else -> resource.getString(R.string.minutes_before, 1)
    }
}
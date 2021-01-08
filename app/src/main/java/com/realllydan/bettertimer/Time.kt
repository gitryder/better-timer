package com.realllydan.bettertimer

import java.util.*

/**
 * # Time
 *
 * `Time` is a Kotlin object that provides a utility method [format] which formats milliseconds as a `String`
 *
 * ## Example:
 * Example formatting 10 minutes in milliseconds as a string:
 *
 * > ``val tenMinutesInMilliseconds = 600000L``
 *
 * > ``val time: String = Time.format(tenMinutesInMilliseconds)``
 * > ``println(time) // Prints "10:00"``
 *
 */
object Time {

    /**
     * A constant representing one second, in milliseconds
     */
    const val MILLIS_ONE_MINUTE = 60000L

    /**
     * Uses the provided [millisToFormat] and returns a string formatted
     * in the usual time format
     *
     * @param millisToFormat the time in milliseconds that needs formatting
     * @return the time in the standard digital clock format, as a formatted string
     */
    @JvmStatic
    fun format(millisToFormat: Long): String {
        val minutes = (millisToFormat / 1000 / 60).toInt()
        val seconds = (millisToFormat / 1000 % 60).toInt()
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
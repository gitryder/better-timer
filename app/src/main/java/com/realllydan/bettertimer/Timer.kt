package com.realllydan.bettertimer

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log

/**
 * # Timer
 *
 * Implement a timer that counts down from the time specified.
 *
 * ## The Problem
 * - The [android.os.CountDownTimer] class calculates the delay for the next tick by considering the duration the last tick took.
 * - This behaviour is reasonable when using the class to performs some tasks upon every timer tick.
 * - However, when used for building a timer, the ticks are highly inaccurate because of the aforementioned reason
 * - Also, since the class isn't necessarily meant for creating timers with, it does not provide an API to pause or reset the timer
 *
 * ## The Solution
 * - This class modifies the [android.os.CountDownTimer], to solve some of the aforementioned problems faced, when using [android.os.CountDownTimer] to implement a timer
 * - It also provides a clean and robust API to interact with the timer through a high-level abstraction class [BetterTimer]
 *
 * @param millisInFuture The time for which the timer shall run, in milliseconds
 */
abstract class Timer(millisInFuture: Long) {

    /**
     * The timer run time in millis
     */
    private val mMillisInFuture = millisInFuture

    /**
     * The interval between ticks. This is one second by default, since that is the standard tick interval for timers
     */
    private val mCountdownInterval = 1000L

    /**
     * boolean representing if the timer was cancelled
     */
    private var mCancelled: Boolean = false
    private var mStopTimeInFuture = 0L

    /**
     * Cancels the running timer.
     */
    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    /**
     * Starts the timer
     */
    @Synchronized
    fun start(): Timer {
        mCancelled = false
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this;
    }

    /**
     * Callback that is invoked on every timer tick
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * Callback that is invoked when the timer has finished running
     */
    abstract fun onFinish()

    companion object {
        const val MSG = 1
    }

    private val mHandler: Handler = object : Handler(Looper.myLooper()!!) {

        override fun handleMessage(msg: Message) {

            synchronized(Timer) {
                if (mCancelled) {
                    return
                }

                val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()
                
                if (millisLeft < mCountdownInterval) {
                    // immediately finish the timer if the time remaining is less than the tick interval
                    onFinish()
                } else {
                    var lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)

                    val lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart
                    var delay = 0L

                    // if the last tick took more than the tick interval, 0 delay is used
                    if (lastTickDuration < mCountdownInterval) {
                        delay = mCountdownInterval - lastTickDuration
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }
}

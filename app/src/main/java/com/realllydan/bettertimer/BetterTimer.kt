package com.realllydan.bettertimer

/**
 * # BetterTimer
 *
 * A better alternative to the [android.os.CountDownTimer] class, for implementing a timer, that provides a robust API
 *
 * @param minInFuture The time in minutes that the timer will run for
 * @param onTimerTickListener The callback that will run
 *
 * &nbsp;
 *
 * Example of implementing a timer that runs for 25 minutes by passing an instance of a callback listener:
 *
 * > ``val timer = BetterTimer(25, Activity.this)``
 *
 * - Call [start] to start the timer
 * - Call [pause] to pause the timer
 * - Call [reset] to reset the timer
 */
class BetterTimer(minInFuture: Int, onTimerTickListener: OnTimerTickListener) {

    /**
     * The Timer start time in millis.
     */
    private val mMillisStartTime = (minInFuture * Time.MILLIS_ONE_MINUTE)

    /**
     * The callback that will run when the timer ticks.
     */
    private val mOnTimerTickListener = onTimerTickListener

    /**
     * The time until the timer finishes, in millis.
     */
    private var mMillisUntilFinished = 0L

    /**
     * boolean representing if the timer is running.
     */
    private var mTimerRunning = false

    private lateinit var timer: Timer

    init {
        mMillisUntilFinished = mMillisStartTime
        mOnTimerTickListener.onTimerTick(Time.format(mMillisUntilFinished))
    }

    /**
     * Start the timer.
     */
    fun start() {
        timer = object: Timer(mMillisUntilFinished) {

            override fun onTick(millisUntilFinished: Long) {
                mMillisUntilFinished = millisUntilFinished
                mOnTimerTickListener.onTimerTick(Time.format(mMillisUntilFinished))
            }

            override fun onFinish() {
                mTimerRunning = false
                reset()
            }
        }.start();

        mTimerRunning = true
    }

    /**
     * Pause the timer if it is running.
     */
    fun pause() {
        if (mTimerRunning) {
            timer.cancel()
            mTimerRunning = false
        }
    }

    /**
     * Reset the timer and invoke the final callback.
     */
    fun reset() {
        if (!mTimerRunning) {
            mMillisUntilFinished = mMillisStartTime
            mOnTimerTickListener.onTimerTick("done!")
        }
    }

    /**
     * Interface definition for a callback to be invoked every time the timer ticks.
     */
    interface OnTimerTickListener {
        /**
         * Called on every timer tick.
         *
         *@param timeUntilFinished The time formatted as a string, used to set the time on your view.
         */
        fun onTimerTick(timeUntilFinished: String)
    }
}
package com.whydah.raramuri.utils

import android.os.CountDownTimer
import com.whydah.raramuri.extensions.toMilliseconds
import com.whydah.raramuri.extensions.toSecond

abstract class CountUpTimer(
    private val secondsInFuture: Long,
    countUpIntervalSeconds: Long
) :
    CountDownTimer(secondsInFuture.toMilliseconds(), countUpIntervalSeconds.toMilliseconds()) {

    abstract fun onCount(count: Int)

    override fun onTick(msUntilFinished: Long) {
        onCount(((secondsInFuture.toMilliseconds() - msUntilFinished).toSecond()).toInt())
    }
}
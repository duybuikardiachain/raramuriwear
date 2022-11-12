package com.kardiachain.raramuri.utils

import android.os.CountDownTimer
import com.kardiachain.raramuri.extensions.toMilliseconds
import com.kardiachain.raramuri.extensions.toSecond

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
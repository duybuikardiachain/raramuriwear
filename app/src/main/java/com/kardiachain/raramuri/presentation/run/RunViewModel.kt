package com.kardiachain.raramuri.presentation.run

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kardiachain.raramuri.presentation.run.model.RunningDataState
import com.kardiachain.raramuri.utils.CommonUtils
import com.kardiachain.raramuri.utils.CountUpTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RunViewModel @Inject constructor(

) : ViewModel() {

    private lateinit var countUpTimerRunning: CountUpTimer

    private val _runningTimeDataState = MutableStateFlow(RunningDataState())
    val runningTimeDataState = _runningTimeDataState.asStateFlow()

    fun checkPermission(context: Context) {

    }

    fun endTimerRunning() {
        if (this::countUpTimerRunning.isInitialized) {
            countUpTimerRunning.cancel()
        }
    }

    fun startTimerRunning(
        endTime: Long,
        startTime: Long,
        currentTime: Long
    ) {
        if (!this::countUpTimerRunning.isInitialized) {
            countUpTimerRunning = object : CountUpTimer(endTime - startTime, 1) {

                override fun onCount(count: Int) {
                    try {
                        val runningTime = CommonUtils.formatReadableDuration(currentTime - startTime + count.toLong())
                        _runningTimeDataState.update { state ->
                            state.copy(
                                runningTime = runningTime
                            )
                        }
                    } catch (_: Exception) {

                    }
                }

                override fun onFinish() {

                }
            }
            countUpTimerRunning.start()
        }
    }
}
package com.example.km.PuntGPSManagment.domain.usecases

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class tempsAturUseCase {

    private val _tempsAturMs = MutableStateFlow(0L)
    val tempsAturMs: StateFlow<Long> = _tempsAturMs

    private var job: Job? = null
    private var startTime = 0L

    fun start(scope: CoroutineScope) {
        if (job?.isActive == true) return

        startTime = SystemClock.elapsedRealtime()
        job = scope.launch {
            while (true) {
                _tempsAturMs.value = SystemClock.elapsedRealtime() - startTime
                delay(10L)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    fun reset() {
        startTime = SystemClock.elapsedRealtime()
        _tempsAturMs.value = 0L
    }
}
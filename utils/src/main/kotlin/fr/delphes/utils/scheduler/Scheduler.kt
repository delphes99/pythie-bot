package fr.delphes.utils.scheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import kotlin.coroutines.CoroutineContext

class Scheduler(
    private val duration: Duration,
    private val doStuff: suspend () -> Unit
) : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun cancel() {
        job.cancel()
    }

    fun start() = launch {
        while (isActive) {
            delay(duration.toMillis())
            doStuff()
        }
    }
}
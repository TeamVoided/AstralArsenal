package org.teamvoided.astralarsenal.coroutine

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val defaultScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

lateinit var mcCoroutineDispatcher: CoroutineDispatcher
    internal set

lateinit var mcCoroutineScope: CoroutineScope
    internal set

fun CoroutineScope.mcLaunch(block: suspend CoroutineScope.() -> Unit) =
    launch(mcCoroutineDispatcher, block = block)

inline fun mcCoroutineTask(
    sync: Boolean = true,
    scope: CoroutineScope = if (sync) mcCoroutineScope else defaultScope,
    occurences: Long = 1,
    period: Duration = 1.ticks,
    delay: Duration = Duration.ZERO,
    crossinline block: suspend CoroutineScope.() -> Unit
) = scope.launch {
    delay(delay)
    for (i in 1..occurences) {
        block(this)
        if (!isActive) break
        if (i < occurences) delay(period)
    }
}

val Int.ticks get() = 50.milliseconds.times(this)

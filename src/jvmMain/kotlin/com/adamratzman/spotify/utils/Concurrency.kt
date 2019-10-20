package com.adamratzman.spotify.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

actual typealias TimeUnit = java.util.concurrent.TimeUnit

actual fun CoroutineScope.schedule(
    quantity: Int, timeUnit: TimeUnit, consumer: () -> Unit
) {
    launch {
        delay(timeUnit.toMillis(quantity.toLong()))
        consumer()
    }
}
package online.marcel.email

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.emailModule() {
    val job = launchPeriodicTask(interval = 1.minutes, scope = this) {
        sendMail()
    }

    monitor.subscribe(ApplicationStopped) {
        job.cancel()
    }
}

fun sendMail() {
    println("Hallo")
}

fun launchPeriodicTask(interval: Duration, scope: CoroutineScope, funktion: suspend () -> Unit): Job {
    return scope.launch(Dispatchers.IO) {
        while (scope.isActive) {
            try {
                funktion()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(interval)
        }
    }
}
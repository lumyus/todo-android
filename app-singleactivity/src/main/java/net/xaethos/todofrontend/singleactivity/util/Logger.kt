package net.xaethos.todofrontend.singleactivity.util

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger(val tag: String) {

    @Inject constructor() : this("XAE")

    fun warn(throwable: Throwable) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, throwable.message)
        }
    }
}

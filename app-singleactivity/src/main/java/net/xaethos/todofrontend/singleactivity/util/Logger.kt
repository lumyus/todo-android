package net.xaethos.todofrontend.singleactivity.util

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger(val tag: String) {

    @Inject constructor() : this("XAE")

    fun warn(throwable: Throwable) {
        Log.w(tag, throwable.message)
    }

    fun d(message: String) {
        Log.d(tag, message)
    }
}

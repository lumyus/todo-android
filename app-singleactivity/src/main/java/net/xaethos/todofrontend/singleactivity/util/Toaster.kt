package net.xaethos.todofrontend.singleactivity.util

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class Toaster @Inject constructor(private val context: Context) {
    fun short(text: CharSequence) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

package net.xaethos.todofrontend.singleactivity.util

import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction

interface ControllerViewHolder {
    val root: View
}

fun Controller.routerTransaction() = RouterTransaction.with(this)

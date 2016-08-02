package net.xaethos.todofrontend.singleactivity.util

import android.support.annotation.IdRes
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.View

data class LayoutAnchor(@IdRes val viewId: Int, val gravity: Int) {
    companion object {
        val NONE = LayoutAnchor(View.NO_ID, Gravity.NO_GRAVITY)
    }
}

var CoordinatorLayout.LayoutParams.anchor: LayoutAnchor
    get() = LayoutAnchor(anchorId, anchorGravity)
    set(value) {
        anchorId = value.viewId
        anchorGravity = value.gravity
    }

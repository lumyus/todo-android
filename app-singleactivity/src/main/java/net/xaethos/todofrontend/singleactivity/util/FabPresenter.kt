package net.xaethos.todofrontend.singleactivity.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.TypedValue
import android.view.Gravity
import net.xaethos.todofrontend.singleactivity.R

interface FabPresenter {
    var enabled: Boolean
    var gravity: Int
    var anchor: LayoutAnchor

    fun setImageResource(@DrawableRes drawableRes: Int)
}

fun presenter(fab: FloatingActionButton): FabPresenter = FabPresenterImpl(fab)

private class FabPresenterImpl(val fab: FloatingActionButton) : FabPresenter {
    override var enabled: Boolean
        get() = fab.isEnabled
        set(enabled) {
            fab.isEnabled = enabled
            fab.backgroundTintList = if (enabled) colorEnabled else colorDisabled
        }

    override var gravity: Int
        get() = layoutParams?.gravity ?: Gravity.NO_GRAVITY
        set(value) {
            layoutParams?.gravity = value
        }

    override var anchor: LayoutAnchor
        get() = layoutParams?.anchor ?: LayoutAnchor.NONE
        set(value) {
            layoutParams?.anchor = value
        }

    override fun setImageResource(drawableRes: Int) = fab.setImageResource(drawableRes)

    private val layoutParams: CoordinatorLayout.LayoutParams?
        get() = fab.layoutParams as? CoordinatorLayout.LayoutParams

    private val colorEnabled: ColorStateList by lazy {
        val typedValue = TypedValue()
        val a = fab.context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)
        a.recycle()

        ColorStateList.valueOf(color)
    }
    private val colorDisabled = ColorStateList.valueOf(Color.GRAY)
}

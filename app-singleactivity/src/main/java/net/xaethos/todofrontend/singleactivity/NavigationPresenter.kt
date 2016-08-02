package net.xaethos.todofrontend.singleactivity

import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import com.jakewharton.rxbinding.view.clicks
import net.xaethos.todofrontend.singleactivity.util.*
import rx.Observable
import javax.inject.Inject

class NavigationPresenter(
        override val root: CoordinatorLayout,
        override val detaches: Observable<Unit>) : ViewPresenter {
    private val appBarLayout: CollapsingToolbarLayout by bindView(R.id.toolbar_layout)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val fab: FloatingActionButton by bindView(R.id.fab)
    private val fabPresenter = presenter(fab)

    var actionBar: ActionBar? = null
    var appBarTitle: CharSequence?
        get() = appBarLayout.title
        set(value) {
            appBarLayout.title = value
        }

    val container by bindView<ViewGroup>(R.id.content_container)

    var fabEnabled by viewEnabled(fab)

    val fabClicks: Observable<Unit>
        get() = fab.clicks().takeUntil(detaches)

    fun configureFab(configure: FabPresenter.() -> Unit) {
        fab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton) {
                fabPresenter.configure()
                fab.show()
            }
        })
    }

    @Inject
    fun setUp(activity: MainActivity) {
        activity.setSupportActionBar(toolbar)
        actionBar = activity.supportActionBar
    }
}

package net.xaethos.todofrontend.singleactivity.tododetail

import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import net.xaethos.todofrontend.singleactivity.MainActivity
import net.xaethos.todofrontend.singleactivity.NavigationPresenter
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.util.LayoutAnchor
import net.xaethos.todofrontend.singleactivity.util.ViewPresenter
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class DetailPresenter(override val root: View) : ViewPresenter, DetailMediator.Presenter {
    @Inject lateinit var navPresenter: NavigationPresenter

    private val detailView: TextView by bindView(R.id.todo_detail)

    override var titleText: CharSequence?
        get() = navPresenter.appBarTitle
        set(value) {
            navPresenter.appBarTitle = value
        }

    override var detailsText by textViewText(detailView)

    override val fabClicks: Observable<Unit>
        get() {
            navPresenter.configureFab {
                setImageResource(R.drawable.ic_edit_white_24dp)
                enabled = true
                gravity = Gravity.CENTER_VERTICAL or GravityCompat.START
                anchor = LayoutAnchor(navPresenter.container.id, Gravity.TOP or GravityCompat.END)
            }
            return navPresenter.fabClicks.takeUntil(detaches)
        }

    @Inject override lateinit var detaches: Observable<Unit>

    @Inject
    fun setUp(activity: MainActivity) {
        navPresenter.actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

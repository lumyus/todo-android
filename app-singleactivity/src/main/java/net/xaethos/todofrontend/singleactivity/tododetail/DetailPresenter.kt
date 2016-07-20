package net.xaethos.todofrontend.singleactivity.tododetail

import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.SingleActivity
import net.xaethos.todofrontend.singleactivity.util.ViewPresenter
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class DetailPresenter(override val root: View) : ViewPresenter, DetailMediator.Presenter {
    private val appBarLayout: CollapsingToolbarLayout by bindView(R.id.toolbar_layout)
    private val toolbar: Toolbar by bindView(R.id.detail_toolbar)
    private val detailView: TextView by bindView(R.id.todo_detail)
    private val fab: FloatingActionButton by bindView(R.id.fab)

    override var titleText: CharSequence?
        get() = appBarLayout.title
        set(value) {
            appBarLayout.title = value
        }

    override var detailsText by textViewText(detailView)

    override val fabClicks: Observable<Unit>
        get() = fab.clicks().takeUntil(detaches)

    @Inject override lateinit var detaches: Observable<Unit>

    @Inject
    fun setUp(activity: SingleActivity) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

package net.xaethos.todofrontend.singleactivity.todoedit

import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.SingleActivity
import net.xaethos.todofrontend.singleactivity.util.Presenter
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import net.xaethos.todofrontend.singleactivity.util.viewEnabled
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class EditPresenter(override val root: View) : Presenter, EditMediator.ViewPresenter {
    private val appBarLayout: CollapsingToolbarLayout by bindView(R.id.toolbar_layout)
    private val toolbar: Toolbar by bindView(R.id.detail_toolbar)
    private val titleField: EditText by bindView(R.id.todo_title)
    private val detailsField: EditText by bindView(R.id.todo_detail)
    private val fab: FloatingActionButton by bindView(R.id.fab)

    override var appBarTitle: CharSequence?
        get() = appBarLayout.title
        set(value) {
            appBarLayout.title = value
        }

    override var titleText by textViewText(titleField)
    override var detailsText by textViewText(detailsField)
    override var fabEnabled by viewEnabled(fab)

    override val titleChanges: Observable<CharSequence>
        get() = titleField.textChanges().takeUntil(unbinds)
    override val detailsChanges: Observable<CharSequence>
        get() = detailsField.textChanges().takeUntil(unbinds)
    override val fabClicks: Observable<Unit>
        get() = fab.clicks().takeUntil(unbinds)

    @Inject override lateinit var unbinds: Observable<Unit>

    @Inject
    fun setUp(activity: SingleActivity) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

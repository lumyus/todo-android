package net.xaethos.todofrontend.singleactivity.todoedit

import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding.widget.textChanges
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
class EditPresenter(override val root: View) : ViewPresenter, EditMediator.Presenter {
    @Inject lateinit var navPresenter: NavigationPresenter

    private val titleField: EditText by bindView(R.id.todo_title)
    private val detailsField: EditText by bindView(R.id.todo_detail)

    override var appBarTitle: CharSequence?
        get() = navPresenter.appBarTitle
        set(value) {
            navPresenter.appBarTitle = value
        }
    override var fabEnabled: Boolean
        get() = navPresenter.fabEnabled
        set(value) {
            configureFab(enabled = value)
        }
    override val fabClicks: Observable<Unit>
        get() {
            configureFab(enabled = true)
            return navPresenter.fabClicks
        }

    private fun configureFab(enabled: Boolean) {
        navPresenter.fabEnabled = enabled
        navPresenter.configureFab {
            setImageResource(R.drawable.ic_done_white_24dp)
            gravity = Gravity.CENTER_VERTICAL or GravityCompat.START
            anchor = LayoutAnchor(navPresenter.container.id, Gravity.TOP or GravityCompat.END)
        }
    }

    override var titleText by textViewText(titleField)
    override var detailsText by textViewText(detailsField)

    override val titleChanges: Observable<CharSequence>
        get() = titleField.textChanges().takeUntil(detaches)
    override val detailsChanges: Observable<CharSequence>
        get() = detailsField.textChanges().takeUntil(detaches)

    @Inject override lateinit var detaches: Observable<Unit>

    @Inject
    fun setUp(activity: MainActivity) {
        navPresenter.actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

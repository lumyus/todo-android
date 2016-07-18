package net.xaethos.todofrontend.singleactivity.tododetail

import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.util.Presenter
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class ToDoDetailPresenter(override val root: View) : Presenter, ToDoDetailMediator.Presenter {
    private val appBarLayout: CollapsingToolbarLayout by bindView(R.id.toolbar_layout)
    private val toolbar: Toolbar by bindView(R.id.detail_toolbar)
    private val detailView: TextView by bindView(R.id.todo_detail)

    override var titleText: CharSequence?
        get() = appBarLayout.title
        set(value) {
            appBarLayout.title = value
        }

    override var detailsText by textViewText(detailView)

    @Inject override lateinit var unbinds: Observable<Unit>
}

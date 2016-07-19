package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.checkedChanges
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.SingleActivity
import net.xaethos.todofrontend.singleactivity.util.*
import rx.Observable
import javax.inject.Inject

/**
 * A presenter handling a `RecyclerView` of to do items.
 *
 * Presenters manipulate a subtree of the view hierarchy. They conform to an interface
 * provided by the mediator, and forward commands and queries to the appropriate views.
 * They also forward user interaction events to the mediator. In this case, they do so
 * via RxJava [Observable]s.
 *
 * We keep our bound views together in the presenter, so we can drop all the
 * references at once. If we were to use [bindView] directly on the controller,
 * a second call to [Controller.onCreateView] wouldn't reinitialize the bindings.
 */
class ToDoListPresenter(override val root: View) : Presenter, ToDoListMediator.ListPresenter {
    val toolbar by bindView<Toolbar>(R.id.toolbar)
    val listView by bindView<RecyclerView>(R.id.todo_list)

    @Inject lateinit var adapter: ToDoListController.Adapter
    @Inject override lateinit var unbinds: Observable<Unit>

    @Inject
    fun setUp(activity: SingleActivity) {
        activity.setSupportActionBar(toolbar)
        toolbar.title = activity.title
        listView.adapter = adapter
    }

    override fun notifyDataSetChanged() = adapter.notifyDataSetChanged()

    class ItemHolder(view: View) : ViewHolderPresenter(view), ToDoListMediator.ItemPresenter {
        private val titleView: TextView by bindView(R.id.text_title)
        private val uriView: TextView by bindView(R.id.text_uri)
        private val completedView: CheckBox by bindView(R.id.chk_completed)

        override var titleText by textViewText(titleView)
        override var urlText by textViewText(uriView)
        override var isChecked by compoundButtonChecked(completedView)

        override val clicks: Observable<Unit> = root.clicks()
        override val checkedChanges: Observable<Boolean> = completedView.checkedChanges()

        @Inject override lateinit var controllerUnbinds: Observable<Unit>
    }
}
package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.detaches
import net.xaethos.todofrontend.singleactivity.CollectionScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.util.Presenter
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
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
class ToDoListPresenter(override val root: View) : Presenter {
    val listView: RecyclerView by bindView<RecyclerView>(R.id.todo_list)

    @Inject fun setUp(adapter: ToDoListAdapter) {
        listView.adapter = adapter
    }

    @CollectionScope
    class ToDoListAdapter @Inject constructor(private val mediator: ToDoListMediator) :
            RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.todo_list_content, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
                mediator.onBindItemPresenter(holder, position)

        override fun getItemCount() = mediator.itemCount
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), ToDoListMediator.ItemPresenter {
        private val idView: TextView by bindView(R.id.id)
        private val contentView: TextView by bindView(R.id.content)

        override var urlText by textViewText(idView)
        override var titleText by textViewText(contentView)

        /*
        Since the lifecycle of a single list item is much shorter than that
        of its container, lets make sure we only emit events until this
        view has been detached.
         */
        override val clicks: Observable<Unit> = view.clicks().takeUntil(view.detaches())
    }
}

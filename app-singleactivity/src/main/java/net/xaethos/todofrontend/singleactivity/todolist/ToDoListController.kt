package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.detaches
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.ToDoListScope
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.util.ControllerViewHolder
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class ToDoListController() : Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewHolder = ViewHolder(inflater.inflate(R.layout.todo_list, container, false))
        val viewComponent = activity.component.toDoListComponentBuilder().build()

        return viewComponent.inject(viewHolder).root
    }

    /*
    We keep our bound views together in a separate class, so we can drop all the
    references at once. If we were to use [bindView] directly on the controller,
    a second call to [onCreateView] wouldn't reinitialize the bindings.
     */
    class ViewHolder(override val root: View) : ControllerViewHolder {
        val listView: RecyclerView by bindView<RecyclerView>(R.id.todo_list)

        @Inject fun setUp(adapter: ToDoListAdapter) {
            listView.adapter = adapter
        }
    }

    /*
    This is the component for this controller's view. Its lifecycle should match the _view's_
    lifecycle. This means we should create a new ViewComponent on each onCreateView
     */
    @ToDoListScope @Subcomponent
    interface ViewComponent {
        fun inject(viewHolder: ViewHolder): ViewHolder

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
        }
    }
}

@ToDoListScope
class ToDoListAdapter @Inject constructor(private val mediator: ToDoListMediator) :
        RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.todo_list_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            mediator.onBindItemPresenter(holder, position)

    override fun getItemCount() = mediator.itemCount

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

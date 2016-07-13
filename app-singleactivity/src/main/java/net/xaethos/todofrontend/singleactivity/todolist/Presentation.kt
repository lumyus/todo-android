package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.rxlifecycle.RxController
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.detaches
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.singletonComponent
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.textViewText
import rx.Observable
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class ToDoListController : RxController() {

    @Inject lateinit var adapter: ToDoListAdapter

    private val listView: RecyclerView by bindView(R.id.todo_list)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.todo_list, container, false)

    override fun onAttach(view: View) {
        super.onAttach(view)
        val module = ToDoListModule(activity)
        singletonComponent.toDoListComponent(module).inject(this)
        listView.adapter = adapter
    }
}

@ToDoListScope
class ToDoListAdapter @Inject constructor(private val mediator: ToDoListMediator) :
        RecyclerView.Adapter<ToDoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ToDoItemViewHolder(inflater.inflate(R.layout.todo_list_content, parent, false))
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) =
            mediator.onBindItemPresenter(holder, position)

    override fun getItemCount() = mediator.itemCount
}

class ToDoItemViewHolder(view: View) : RecyclerView.ViewHolder(view), ToDoListMediator.ItemPresenter {
    private val idView: TextView by bindView(R.id.id)
    private val contentView: TextView by bindView(R.id.content)

    override var urlText by textViewText(idView)
    override var titleText by textViewText(contentView)

    override val clicks: Observable<Int> = view.clicks().takeUntil(view.detaches()).map { adapterPosition }
}

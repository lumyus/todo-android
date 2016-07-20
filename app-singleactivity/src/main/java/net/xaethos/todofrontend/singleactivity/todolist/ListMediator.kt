package net.xaethos.todofrontend.singleactivity.todolist

import android.support.annotation.VisibleForTesting
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.util.Logger
import net.xaethos.todofrontend.singleactivity.util.ViewPresenter
import rx.Observable
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@ControllerScope
class ListMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: TodoDataSource
    @Inject lateinit var logger: Logger

    @VisibleForTesting var todoList: List<Todo> = emptyList()

    val itemCount: Int
        get() = todoList.size

    fun itemId(index: Int): Long = todoList[index].uri.hashCode().toLong()

    fun bindListPresenter(presenter: ListPresenter) {
        dataSource.all.takeUntil(presenter.detaches).subscribe { newItems ->
            todoList = newItems
            presenter.notifyDataSetChanged()
        }

        presenter.fabClicks.subscribe { navigator.pushCreateController() }
    }

    fun bindItemPresenter(presenter: ItemPresenter, position: Int) {
        // First get the data to bind to
        val todo = todoList[position]

        // We now set the state of the presenter to reflect its position
        presenter.titleText = todo.title
        presenter.urlText = todo.uri
        presenter.isChecked = todo.completed

        // Finally, we subscribe to UI events
        presenter.clicks.subscribeWith {
            onNext { navigator.pushDetailController(todo) }
            onError { logger.warn(it) }
        }
        presenter.checkedChanges.takeUntil(presenter.detaches).subscribe { checked ->
            dataSource.put(todo.copy(completed = checked))
        }
    }

    /**
     * The navigator interfaces are how the mediator requests for a change in the app flow.
     * It'll almost certainly be implemented by a Controller, since it has access to the router,
     * but the exact way the flow changes may be different depending on the current app
     * configuration. i.e. a phone may push a new controller on top, while a tablet may place it on
     * a detail view.
     */
    interface Navigator {
        fun pushDetailController(todo: Todo)
        fun pushCreateController()
    }

    interface ListPresenter : ViewPresenter {
        fun notifyDataSetChanged()
        val fabClicks: Observable<Unit>
    }

    /**
     * The presenter interfaces are what the mediator needs to control and react to the UI. The
     * exact view structure is not important as long as these methods are available.
     */
    interface ItemPresenter : ViewPresenter {
        /*
        These are the presenters "controls." They let us check and modify
        its state.
         */
        var titleText: CharSequence?
        var urlText: CharSequence?
        var isChecked: Boolean

        /*
        These are the presenters event emitters.
         */
        val clicks: Observable<Unit>
        val checkedChanges: Observable<Boolean>
    }
}

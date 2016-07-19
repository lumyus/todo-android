package net.xaethos.todofrontend.singleactivity.todolist

import android.support.annotation.VisibleForTesting
import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.CollectionScope
import net.xaethos.todofrontend.singleactivity.util.Logger
import net.xaethos.todofrontend.singleactivity.util.Presenter
import rx.Observable
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@CollectionScope
class ToDoListMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: ToDoDataSource
    @Inject lateinit var logger: Logger

    @VisibleForTesting var toDos: List<ToDoData> = emptyList()

    val itemCount: Int
        get() = toDos.size

    fun itemId(index: Int): Long = toDos[index].uri.hashCode().toLong()

    fun bindListPresenter(presenter: ListPresenter) =
            dataSource.all.takeUntil(presenter.unbinds).subscribeWith {
                onNext { newItems ->
                    toDos = newItems
                    presenter.notifyDataSetChanged()
                }
            }

    fun bindItemPresenter(presenter: ItemPresenter, position: Int) {
        // First get the data to bind to
        val toDo = toDos[position]

        // We now set the state of the presenter to reflect its position
        presenter.titleText = toDo.title
        presenter.urlText = toDo.uri
        presenter.isChecked = toDo.completed

        // Finally, we subscribe to UI events
        presenter.clicks.takeUntil(presenter.unbinds).subscribeWith {
            onNext { navigator.pushDetailController(toDo) }
            onError { logger.warn(it) }
        }
        presenter.checkedChanges.takeUntil(presenter.unbinds).subscribe { checked ->
            dataSource.put(toDo.copy(completed = checked))
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
        fun pushDetailController(toDo: ToDoData)
    }

    interface ListPresenter : Presenter {
        fun notifyDataSetChanged()
    }

    /**
     * The presenter interfaces are what the mediator needs to control and react to the UI. The
     * exact view structure is not important as long as these methods are available.
     */
    interface ItemPresenter : Presenter {
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

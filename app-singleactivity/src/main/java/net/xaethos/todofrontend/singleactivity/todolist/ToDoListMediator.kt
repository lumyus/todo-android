package net.xaethos.todofrontend.singleactivity.todolist

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.ToDoListScope
import net.xaethos.todofrontend.singleactivity.util.Logger
import net.xaethos.todofrontend.singleactivity.util.Toaster
import rx.Observable
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@ToDoListScope
class ToDoListMediator @Inject constructor() {
    @Inject lateinit var dataSource: ToDoDataSource
    @Inject lateinit var logger: Logger
    @Inject lateinit var toaster: Toaster

    private val toDos: List<ToDoData>
        get() = dataSource.all

    val itemCount: Int
        get() = toDos.size

    fun onBindItemPresenter(presenter: ItemPresenter, position: Int) {
        // First get the item to bind
        val toDo = toDos[position]

        // We now set the state of the presenter to reflect its position
        presenter.titleText = toDo.title
        presenter.urlText = toDo.uri

        // Finally, we subscribe to UI events
        presenter.clicks.subscribeWith {
            onNext { toaster.short("Tapped on '${toDo.title}'!") }
            onError { logger.warn(it) }
        }
    }

    /*
    The presenter interfaces are what the mediator needs to control and react
    to the UI. The exact view structure is not important as long as these methods
    are available.
     */
    interface ItemPresenter {
        /*
        These are the presenters "controls." They let us check and modify
        its state.
         */
        var titleText: CharSequence?
        var urlText: CharSequence?

        /*
        These are the presenters event emitters.
         */
        val clicks: Observable<Unit>
    }
}

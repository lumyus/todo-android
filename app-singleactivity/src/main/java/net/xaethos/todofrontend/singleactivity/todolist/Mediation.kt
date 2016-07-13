package net.xaethos.todofrontend.singleactivity.todolist

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
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
        get() = dataSource.allItems

    val itemCount: Int
        get() = toDos.size

    fun onBindItemPresenter(presenter: ItemPresenter, position: Int) {
        val toDo = toDos[position]
        presenter.titleText = toDo.title
        presenter.urlText = toDo.url

        presenter.clicks.map { toDos[it].title }.subscribeWith {
            onNext { toaster.short("Tapped $it!") }
            onError { logger.warn(it) }
        }
    }

    interface ItemPresenter {
        var titleText: CharSequence?
        var urlText: CharSequence?
        val clicks: Observable<Int>
    }
}

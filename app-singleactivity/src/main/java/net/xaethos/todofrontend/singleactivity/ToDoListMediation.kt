package net.xaethos.todofrontend.singleactivity

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@ToDoListScope
class ToDoListMediator @Inject constructor() {
    @Inject lateinit var dataSource: ToDoDataSource

    private val toDos: List<ToDoData>
        get() = dataSource.allItems

    val itemCount: Int
        get() = toDos.size

    fun onBindItemPresenter(presenter: ItemPresenter, position: Int) {
        val toDo = toDos[position]
        presenter.titleText = toDo.title
        presenter.urlText = toDo.url
    }

    interface ListPresenter

    interface ItemPresenter {
        var titleText: CharSequence?
        var urlText: CharSequence?
    }
}

package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.ToDoItemScope
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@ToDoItemScope
class ToDoDetailMediator @Inject constructor() {
    lateinit var toDo: ToDoData

    fun onBindPresenter(presenter: Presenter) {
        presenter.titleText = toDo.title
        presenter.detailsText = toDo.details
    }

    interface Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?
    }
}

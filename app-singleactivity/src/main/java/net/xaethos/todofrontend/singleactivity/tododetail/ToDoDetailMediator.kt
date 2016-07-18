package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ItemScope
class ToDoDetailMediator @Inject constructor() {
    @Inject lateinit var dataSource: ToDoDataSource

    fun bindPresenter(presenter: Presenter, uri: String) {
        val toDo = dataSource[uri]
        presenter.titleText = toDo?.title
        presenter.detailsText = toDo?.details
    }

    interface Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?
    }
}

package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import javax.inject.Inject

/**
 * Mediator: All business logic goes here
 */
@ItemScope
class ToDoDetailMediator @Inject constructor(val presenter: Presenter, val uri: String) {
    @Inject lateinit var dataSource: ToDoDataSource

    @Inject
    fun fetchData() {
        bindPresenter(dataSource[uri])
    }

    fun bindPresenter(toDo: ToDoData?) {
        presenter.titleText = toDo?.title
        presenter.detailsText = toDo?.details
    }

    interface Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?
    }
}

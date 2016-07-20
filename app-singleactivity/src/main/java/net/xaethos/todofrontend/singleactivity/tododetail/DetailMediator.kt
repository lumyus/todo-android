package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.util.ViewPresenter
import rx.Observable
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ControllerScope
class DetailMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: TodoDataSource

    fun bindPresenter(presenter: Presenter, uri: String) {
        dataSource[uri].takeUntil(presenter.detaches).subscribe { todo ->
            presenter.titleText = todo.title
            presenter.detailsText = todo.details
            presenter.fabClicks.subscribe { navigator.pushEditController(todo) }
        }
    }

    interface Navigator {
        fun pushEditController(todo: Todo)
    }

    interface Presenter : ViewPresenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?

        val fabClicks: Observable<Unit>
    }
}

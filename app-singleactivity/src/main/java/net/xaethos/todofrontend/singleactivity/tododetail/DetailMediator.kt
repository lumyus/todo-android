package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.util.Presenter
import rx.Observable
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ItemScope
class DetailMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: TodoDataSource

    fun bindPresenter(presenter: ViewPresenter, uri: String) {
        dataSource[uri].takeUntil(presenter.unbinds).subscribe { todo ->
            presenter.titleText = todo.title
            presenter.detailsText = todo.details
            presenter.fabClicks.subscribe { navigator.pushEditController(todo) }
        }
    }

    interface Navigator {
        fun pushEditController(todo: Todo)
    }

    interface ViewPresenter : Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?

        val fabClicks: Observable<Unit>
    }
}

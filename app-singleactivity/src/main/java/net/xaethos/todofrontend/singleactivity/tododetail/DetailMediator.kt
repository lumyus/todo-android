package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.util.Presenter
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ItemScope
class DetailMediator @Inject constructor() {
    @Inject lateinit var dataSource: TodoDataSource

    fun bindPresenter(presenter: ViewPresenter, uri: String) =
            dataSource[uri].takeUntil(presenter.unbinds).subscribeWith {
                onNext { todo ->
                    presenter.titleText = todo.title
                    presenter.detailsText = todo.details
                }
            }

    interface ViewPresenter : Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?
    }
}

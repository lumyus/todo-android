package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.util.Presenter
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ItemScope
class ToDoDetailMediator @Inject constructor() {
    @Inject lateinit var dataSource: ToDoDataSource

    fun bindPresenter(presenter: ViewPresenter, uri: String) =
            dataSource[uri].takeUntil(presenter.unbinds).subscribeWith {
                onNext { toDo ->
                    presenter.titleText = toDo.title
                    presenter.detailsText = toDo.details
                }
            }

    interface ViewPresenter : Presenter {
        var titleText: CharSequence?
        var detailsText: CharSequence?
    }
}

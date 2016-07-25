package net.xaethos.todofrontend.singleactivity.todoedit

import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.util.ViewPresenter
import rx.Observable
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ControllerScope
class EditMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: TodoDataSource

    var titleValue: String = ""
    var detailsValue: String = ""

    fun bindPresenter(presenter: Presenter, uri: String?) =
            if (uri == null) bindForCreate(presenter) else bindForEdit(presenter, uri)

    private fun bindForCreate(presenter: Presenter) {
        presenter.appBarTitle = "New todo"
        bindFields(presenter, null)
    }

    private fun bindForEdit(presenter: Presenter, uri: String) {
        presenter.appBarTitle = "Edit todo"
        dataSource[uri].takeUntil(presenter.detaches).first().subscribeWith {
            onNext { todo ->
                presenter.titleText = todo.title
                presenter.detailsText = todo.details
                bindFields(presenter, todo)
            }
        }
    }

    private fun bindFields(presenter: Presenter, originalTodo: Todo?) {
        presenter.fabClicks
                .doOnNext { presenter.fabEnabled = false }
                .subscribe { submit(originalTodo, titleValue, detailsValue) }

        presenter.titleChanges
                .map { it.toString() }
                .subscribe { titleValue = it }

        presenter.titleChanges
                .map(CharSequence?::isNullOrBlank)
                .distinctUntilChanged()
                .subscribe { isBlank -> presenter.fabEnabled = !isBlank }

        presenter.detailsChanges
                .map { it.toString() }
                .subscribe { detailsValue = it }
    }

    private fun submit(originalTodo: Todo?, titleValue: String, detailsValue: String) {
        if (titleValue.isBlank()) return

        if (originalTodo == null) {
            dataSource.create(titleValue, detailsValue)
        } else {
            dataSource.put(originalTodo.copy(title = titleValue, details = detailsValue))
        }
        navigator.navigateBack()
    }

    interface Navigator {
        fun navigateBack()
    }

    interface Presenter : ViewPresenter {
        var appBarTitle: CharSequence?

        var titleText: CharSequence?
        var detailsText: CharSequence?
        var fabEnabled: Boolean

        val titleChanges: Observable<CharSequence>
        val detailsChanges: Observable<CharSequence>
        val fabClicks: Observable<Unit>
    }
}

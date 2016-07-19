package net.xaethos.todofrontend.singleactivity.todoedit

import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.util.Presenter
import rx.Observable
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject

/**
 * Mediator: uses business logic to binding data to views
 */
@ItemScope
class EditMediator @Inject constructor(val navigator: Navigator) {
    @Inject lateinit var dataSource: TodoDataSource

    var originalTodo: Todo? = null
    var titleValue: String = ""
    var detailsValue: String = ""

    fun bindPresenter(presenter: ViewPresenter, uri: String?) =
            if (uri == null) bindForCreate(presenter) else bindForEdit(presenter, uri)

    private fun bindForCreate(presenter: ViewPresenter) {
        presenter.appBarTitle = "New todo"
        bindFields(presenter)
    }

    private fun bindForEdit(presenter: ViewPresenter, uri: String) {
        presenter.appBarTitle = "Edit todo"
        dataSource[uri].first().takeUntil(presenter.unbinds).subscribeWith {
            onNext { todo ->
                originalTodo = todo
                presenter.titleText = todo.title
                presenter.detailsText = todo.details
                bindFields(presenter)
            }
        }
    }

    private fun bindFields(presenter: ViewPresenter) {
        presenter.fabEnabled = !presenter.titleText.isNullOrBlank()

        presenter.titleChanges
                .doOnNext { presenter.fabEnabled = !it.isBlank() }
                .map { it.toString() }
                .subscribe { titleValue = it }

        presenter.detailsChanges
                .map { it.toString() }
                .subscribe { detailsValue = it }

        presenter.fabClicks
                .doOnNext { presenter.fabEnabled = false }
                .subscribe { submit(originalTodo, titleValue, detailsValue) }
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

    interface ViewPresenter : Presenter {
        var appBarTitle: CharSequence?

        var titleText: CharSequence?
        var detailsText: CharSequence?
        var fabEnabled: Boolean

        val titleChanges: Observable<CharSequence>
        val detailsChanges: Observable<CharSequence>
        val fabClicks: Observable<Unit>
    }
}

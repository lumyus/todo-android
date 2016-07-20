package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.singleactivity.test.mock
import net.xaethos.todofrontend.singleactivity.test.stub
import net.xaethos.todofrontend.singleactivity.test.withSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class DetailMediatorTest {
    val uri = "http://example.com/todo/13"
    val todo = Todo(uri, "Title", "Details")

    val fabClickSubject = PublishSubject<Unit>()

    val presenter = mock<DetailMediator.Presenter> {
        stub(fabClicks).withSubject(fabClickSubject)
        stub(detaches).thenReturn(Observable.never())
    }

    val navigator = mock<DetailMediator.Navigator>()
    val mediator = DetailMediator(navigator)

    @Before
    fun setUp() {
        mediator.dataSource = mock {
            stub(get(uri)).withSubject(BehaviorSubject(todo))
        }
    }

    @Test
    fun bindPresenter() {
        mediator.bindPresenter(presenter, uri)

        verify(presenter).titleText = "Title"
        verify(presenter).detailsText = "Details"
    }

    @Test
    fun onFabClick_editTodo() {
        mediator.bindPresenter(presenter, uri)
        fabClickSubject.onNext(Unit)
        verify(navigator).pushEditController(todo)
    }
}

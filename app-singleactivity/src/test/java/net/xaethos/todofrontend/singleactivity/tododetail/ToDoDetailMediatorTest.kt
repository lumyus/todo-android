package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.test.mock
import net.xaethos.todofrontend.singleactivity.test.stub
import net.xaethos.todofrontend.singleactivity.test.withSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToDoDetailMediatorTest {
    val uri = "http://example.com/todo/13"
    val toDo = ToDoData(uri, "Title", "Details")

    val mediator = ToDoDetailMediator()

    @Before
    fun setUp() {
        mediator.dataSource = mock {
            stub(get(uri)).withSubject(BehaviorSubject(toDo))
        }
    }

    @Test
    fun bindPresenter() {
        val presenter = mock<ToDoDetailMediator.ViewPresenter> {
            stub(unbinds).thenReturn(Observable.never())
        }

        mediator.bindPresenter(presenter, uri)

        verify(presenter).titleText = "Title"
        verify(presenter).detailsText = "Details"
    }

    @Test
    fun bindPresenter_unsubscribesOnUnbind() {
        val presenter = mock<ToDoDetailMediator.ViewPresenter>()
        val unbindSubject = stub(presenter.unbinds).withSubject(PublishSubject())

        val subscription = mediator.bindPresenter(presenter, uri)
        assertFalse(subscription.isUnsubscribed)

        unbindSubject.onNext(Unit)
        assertTrue(subscription.isUnsubscribed)
    }
}

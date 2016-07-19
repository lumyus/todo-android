package net.xaethos.todofrontend.singleactivity.todolist

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.test.mock
import net.xaethos.todofrontend.singleactivity.test.stub
import net.xaethos.todofrontend.singleactivity.test.withSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import rx.lang.kotlin.toSingletonObservable
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToDoListMediatorTest {

    val data = listOf(
            todo(0),
            todo(1),
            todo(2),
            todo(3)
    )

    val clickSubject = PublishSubject<Unit>()
    val unbindSubject = PublishSubject<Unit>()

    val navigator: ToDoListMediator.Navigator = mock()
    val dataSource: ToDoDataSource = mock {
        stub(all).withSubject(BehaviorSubject(data))
    }
    val listPresenter: ToDoListMediator.ListPresenter = mock {
        stub(unbinds).withSubject(unbindSubject)
    }
    val itemPresenter: ToDoListMediator.ItemPresenter = mock {
        stub(clicks).withSubject(clickSubject)
        stub(unbinds).withSubject(unbindSubject)
    }

    val mediator = ToDoListMediator(navigator)

    @Before
    fun setUp() {
        mediator.dataSource = dataSource
    }

    @Test
    fun bindListPresenter() {
        mediator.bindListPresenter(listPresenter)

        verify(listPresenter).notifyDataSetChanged()
        mediator.toDos shouldMatch equalTo(data)
    }

    @Test
    fun bindListPresenter_unsubscribesOnUnbind() {
        val subscription = mediator.bindListPresenter(listPresenter)
        assertFalse(subscription.isUnsubscribed)

        unbindSubject.onNext(Unit)
        assertTrue(subscription.isUnsubscribed)
    }

    @Test
    fun bindItemPresenter() {
        mediator.toDos = data
        mediator.bindItemPresenter(itemPresenter, 1)

        verify(itemPresenter).titleText = "To Do 1"
        verify(itemPresenter).urlText = "http://example.com/todo/1"
    }

    @Test
    fun bindItemPresenter_unsubscribesOnUnbind() {
        mediator.toDos = data

        val subscription = mediator.bindItemPresenter(itemPresenter, 1)
        assertFalse(subscription.isUnsubscribed)

        unbindSubject.onNext(Unit)
        assertTrue(subscription.isUnsubscribed)
    }

    @Test
    fun onItemClick_showDetails() {
        val presenter: ToDoListMediator.ItemPresenter = mock {
            `when`(clicks).thenReturn(Unit.toSingletonObservable())
            `when`(unbinds).thenReturn(Observable.never())
        }

        mediator.toDos = data
        mediator.bindItemPresenter(presenter, 2)

        verify(navigator).pushDetailController(data[2])
    }

    @Test
    fun itemCount() {
        mediator.toDos = data
        assertEquals(4, mediator.itemCount)
    }
}

private fun todo(id: Int) = ToDoData("http://example.com/todo/$id", "To Do $id")

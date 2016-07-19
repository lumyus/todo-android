package net.xaethos.todofrontend.singleactivity.todolist

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import net.xaethos.todofrontend.singleactivity.test.mock
import net.xaethos.todofrontend.singleactivity.test.stub
import net.xaethos.todofrontend.singleactivity.test.withSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListMediatorTest {

    val data = listOf(
            todo(0),
            todo(1),
            todo(2),
            todo(3)
    )

    val dataSubject = BehaviorSubject(data)
    val clickSubject = PublishSubject<Unit>()
    val checkedSubject = PublishSubject<Boolean>()
    val unbindSubject = PublishSubject<Unit>()

    val navigator: ListMediator.Navigator = mock()
    val dataSource: TodoDataSource = mock {
        stub(all).withSubject(dataSubject)
    }
    val listPresenter: ListMediator.ListPresenter = mock {
        stub(unbinds).withSubject(unbindSubject)
    }
    val itemPresenter: ListMediator.ItemPresenter = mock {
        stub(clicks).withSubject(clickSubject)
        stub(checkedChanges).withSubject(checkedSubject)
        stub(unbinds).withSubject(unbindSubject)
    }

    val mediator = ListMediator(navigator)

    @Before
    fun setUp() {
        mediator.dataSource = dataSource
    }

    @Test
    fun bindListPresenter() {
        mediator.bindListPresenter(listPresenter)

        verify(listPresenter).notifyDataSetChanged()
        mediator.todoList shouldMatch equalTo(data)
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
        mediator.todoList = listOf(
                Todo("todo/10", "title", completed = true),
                Todo("todo/1", "To Do 1")
        )

        mediator.bindItemPresenter(itemPresenter, 0)
        verify(itemPresenter).titleText = "title"
        verify(itemPresenter).urlText = "todo/10"
        verify(itemPresenter).isChecked = true

        mediator.bindItemPresenter(itemPresenter, 1)
        verify(itemPresenter).titleText = "To Do 1"
        verify(itemPresenter).urlText = "todo/1"
        verify(itemPresenter).isChecked = false
    }

    @Test
    fun onItemClick_showDetails() {
        mediator.todoList = data
        mediator.bindItemPresenter(itemPresenter, 2)

        clickSubject.onNext(Unit)

        verify(navigator).pushDetailController(data[2])
    }

    @Test
    fun onItemChecked_updateData() {
        val item = Todo("todo/13", "doable")
        mediator.todoList = listOf(item)
        mediator.bindItemPresenter(itemPresenter, 0)

        checkedSubject.onNext(true)

        verify(dataSource).put(item.copy(completed = true))
    }

    @Test
    fun itemCount() {
        mediator.todoList = data
        assertEquals(4, mediator.itemCount)
    }

    @Test
    fun itemId() {
        mediator.todoList = data
        assertEquals(data[2].uri.hashCode().toLong(), mediator.itemId(2))
    }
}

private fun todo(id: Int) = Todo("http://example.com/todo/$id", "To Do $id")

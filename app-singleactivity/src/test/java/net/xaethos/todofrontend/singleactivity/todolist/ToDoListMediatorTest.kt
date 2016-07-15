package net.xaethos.todofrontend.singleactivity.todolist

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.datasource.ToDoDataSource
import net.xaethos.todofrontend.singleactivity.test.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import rx.lang.kotlin.emptyObservable
import rx.lang.kotlin.toSingletonObservable
import kotlin.test.assertEquals

class ToDoListMediatorTest {

    val data = listOf(
            todo(0),
            todo(1),
            todo(2),
            todo(3)
    )

    val navigator: ToDoListMediator.Navigator = mock()
    val dataSource: ToDoDataSource = mock {
        `when`(all).thenReturn(data)
    }

    val mediator = ToDoListMediator(navigator)

    @Before
    fun setUp() {
        mediator.dataSource = dataSource
    }

    @Test
    fun onBindItemPresenter() {
        val presenter: ToDoListMediator.ItemPresenter = mock {
            `when`(clicks).thenReturn(emptyObservable())
        }

        mediator.onBindItemPresenter(presenter, 1)

        verify(presenter).titleText = "To Do 1"
        verify(presenter).urlText = "http://example.com/todo/1"
    }

    @Test
    fun onItemClick_showDetails() {
        val presenter: ToDoListMediator.ItemPresenter = mock {
            `when`(clicks).thenReturn(Unit.toSingletonObservable())
        }

        mediator.onBindItemPresenter(presenter, 1)

        verify(navigator).pushDetailController(data[1])
    }

    @Test
    fun itemCount() {
        assertEquals(4, mediator.itemCount)
    }
}

private fun todo(id: Int) = ToDoData("http://example.com/todo/$id", "To Do $id")

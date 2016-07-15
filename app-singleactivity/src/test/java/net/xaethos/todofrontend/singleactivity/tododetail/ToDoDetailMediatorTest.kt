package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.test.mock
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ToDoDetailMediatorTest {
    val uri = "http://example.com/todo/13"
    val toDo = ToDoData(uri, "Title", "Details")

    @Test
    fun fetchData() {
        val presenter = mock<ToDoDetailMediator.Presenter>()
        val mediator = ToDoDetailMediator(presenter, uri)
        mediator.dataSource = mock {
            `when`(get(uri)).thenReturn(toDo)
        }

        mediator.fetchData()

        verify(presenter).titleText = "Title"
        verify(presenter).detailsText = "Details"
    }

    @Test
    fun bindPresenter() {
        val presenter = mock<ToDoDetailMediator.Presenter>()
        val mediator = ToDoDetailMediator(presenter, uri)

        mediator.bindPresenter(toDo)

        verify(presenter).titleText = "Title"
        verify(presenter).detailsText = "Details"
    }
}

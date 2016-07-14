package net.xaethos.todofrontend.singleactivity.tododetail

import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.test.mock
import org.junit.Test
import org.mockito.Mockito.verify

class ToDoDetailMediatorTest {

    @Test
    fun testOnBindPresenter() {
        val presenter = mock<ToDoDetailMediator.Presenter>()
        val mediator = ToDoDetailMediator()
        mediator.toDo = ToDoData("http://example.com/todo/13", "Title", "Details")

        mediator.onBindPresenter(presenter)

        verify(presenter).titleText = "Title"
        verify(presenter).detailsText = "Details"
    }
}

package net.xaethos.todofrontend.datasource

import com.natpryce.hamkrest.isEmpty
import com.natpryce.hamkrest.should.shouldMatch
import net.xaethos.todofrontend.datasource.test.emits
import net.xaethos.todofrontend.datasource.test.onNextEvents
import net.xaethos.todofrontend.datasource.test.shouldEqual
import net.xaethos.todofrontend.datasource.test.withTestSubscriber
import org.junit.Test
import kotlin.test.assertNotEquals

class ModuleDataSourceTest {

    val seededItems = listOf(
            Todo(uri = "todo/1", title = "Give a talk on To Do app"),
            Todo(uri = "todo/2", title = "Do stuff", details = "I have stuff to do"),
            Todo(uri = "todo/0", title = "Write To Do app", completed = true)
    )

    val dataSource = DataModule().dataSource()

    @Test
    fun initialItems() {
        dataSource.all shouldMatch emits(seededItems)
    }

    @Test
    fun observeAll() {
        dataSource.all.withTestSubscriber { subscriber ->
            subscriber shouldMatch onNextEvents(seededItems)
            subscriber.assertNotCompleted()
        }
    }

    @Test
    fun getByIndex() {
        dataSource[1].withTestSubscriber { subscriber ->
            subscriber shouldMatch onNextEvents(seededItems[1])
            subscriber.assertNotCompleted()
        }
    }

    @Test
    fun getByUri() {
        dataSource["todo/0"].withTestSubscriber { subscriber ->
            subscriber shouldMatch onNextEvents(seededItems[2])
            subscriber.assertNotCompleted()
        }
    }

    @Test
    fun getByUri_completesIfMissingUri() {
        dataSource["does not exist"].withTestSubscriber { subscriber ->
            subscriber.onNextEvents shouldMatch isEmpty
            subscriber.assertCompleted()
        }
    }

    @Test
    fun create() {
        dataSource.create("new title", "details").withTestSubscriber { subscriber ->
            subscriber.assertNotCompleted()
            val createdItem = subscriber.onNextEvents.single()

            createdItem shouldEqual
                    Todo(createdItem.uri, "new title", "details", completed = false)
        }
    }

    @Test
    fun create_emitsListUpdate() {
        val createdItem = dataSource.create("new title", "details").toBlocking().first()
        dataSource.all shouldMatch emits(listOf(
                seededItems[0],
                seededItems[1],
                createdItem,
                seededItems[2]
        ))
    }

    @Test
    fun create_generatesDifferingUris() {
        val fooItem = dataSource.create("title").toBlocking().first()
        val barItem = dataSource.create("title").toBlocking().first()

        assertNotEquals(fooItem.uri, barItem.uri)
    }

    @Test
    fun put_whenExistingUri_updatesItemInPlace() {
        val updatedItem = Todo(uri = "todo/1", title = "Give a talk on application testing")
        dataSource.put(updatedItem)
        dataSource.all shouldMatch emits(listOf(
                updatedItem,
                seededItems[1],
                seededItems[2]
        ))
    }

    @Test
    fun put_whenExistingUri_emitsUpdatedItem() {
        val originalItem = seededItems[0]
        val updatedItem = originalItem.copy(title = "new title")

        dataSource[originalItem.uri].withTestSubscriber { subscriber ->
            dataSource.put(updatedItem)

            subscriber shouldMatch onNextEvents(originalItem, updatedItem)
        }
    }

    @Test
    fun put_noOpWhenNoChange() {
        dataSource.all.withTestSubscriber { subscriber ->
            dataSource.put(seededItems[0])
            subscriber shouldMatch onNextEvents(seededItems)
        }
    }

    @Test
    fun put_whenCompletionChanges_updatesPosition() {
        val completedItem = seededItems[0].copy(completed = true)
        val uncompletedItem = seededItems[2].copy(completed = false)

        dataSource.all.withTestSubscriber { subscriber ->
            dataSource.put(completedItem)
            dataSource.put(uncompletedItem)

            subscriber shouldMatch onNextEvents(
                    listOf(seededItems[0], seededItems[1], seededItems[2]),
                    listOf(seededItems[1], completedItem, seededItems[2]),
                    listOf(seededItems[1], uncompletedItem, completedItem)
            )
        }
    }

    @Test
    fun put_whenNewUri_insertsBeforeCompleted() {
        val newItem = Todo(uri = "todo/new", title = "another to do")
        dataSource.put(newItem)
        dataSource.all shouldMatch emits(listOf(
                seededItems[0],
                seededItems[1],
                newItem,
                seededItems[2]
        ))
    }

    @Test
    fun put_whenNewUri_andCompleted_insertsAtEnd() {
        val newItem = Todo(uri = "todo/new", title = "completed to do", completed = true)
        dataSource.put(newItem)
        dataSource.all shouldMatch emits(listOf(
                seededItems[0],
                seededItems[1],
                seededItems[2],
                newItem
        ))
    }

    @Test
    fun delete() {
        dataSource.delete(seededItems[2].uri)
        dataSource.all shouldMatch emits(listOf(
                seededItems[0],
                seededItems[1]
        ))
    }

    @Test
    fun delete_completesItemObservable() {
        dataSource["todo/0"].withTestSubscriber { subscriber ->
            dataSource.delete("todo/0")
            subscriber shouldMatch onNextEvents(seededItems[2])
            subscriber.assertCompleted()
        }
    }

    @Test
    fun delete_noOpOnMissingUri() {
        dataSource.all.withTestSubscriber { subscriber ->
            dataSource.delete("non-existent")
            subscriber shouldMatch onNextEvents(seededItems)
        }
    }
}

package net.xaethos.todofrontend.datasource

import org.junit.Test
import kotlin.test.assertNotEquals
import kotlin.test.expect

class ModuleDataSourceTest {

    val seededItems = listOf(
            ToDoData(uri = "todo/1", title = "Give a talk on To Do app"),
            ToDoData(uri = "todo/2", title = "Do stuff", details = "I have stuff to do"),
            ToDoData(uri = "todo/0", title = "Write To Do app", completed = true)
    )

    val dataSource = DataModule().dataSource()

    @Test
    fun initialItems() {
        expect(seededItems) { dataSource.all }
    }

    @Test
    fun getByIndex() {
        expect(seededItems[1]) { dataSource[1] }
    }

    @Test
    fun getByUri() {
        expect(seededItems[2]) { dataSource["todo/0"] }
    }

    @Test
    fun create() {
        val createdItem = dataSource.create("new title", "details")

        expect(ToDoData(createdItem.uri, "new title", "details", completed = false)) { createdItem }

        expect(listOf(
                seededItems[0],
                seededItems[1],
                createdItem,
                seededItems[2]
        )) {
            dataSource.all
        }
    }

    @Test
    fun create_generatesDifferingUris() {
        val fooItem = dataSource.create("title")
        val barItem = dataSource.create("title")

        assertNotEquals(fooItem.uri, barItem.uri)
    }

    @Test
    fun put_whenExistingUri_updatesItem() {
        val updatedItem = ToDoData(uri = "todo/1", title = "Give a talk on application testing")

        expect(listOf(
                updatedItem,
                seededItems[1],
                seededItems[2]
        )) {
            dataSource.put(updatedItem)
            dataSource.all
        }
    }

    @Test
    fun put_whenCompletionChanges_updatesPosition() {
        val completedItem = seededItems[0].copy(completed = true)
        val uncompletedItem = seededItems[2].copy(completed = false)

        expect(listOf(
                seededItems[1],
                completedItem,
                seededItems[2]
        )) {
            dataSource.put(completedItem)
            dataSource.all
        }

        expect(listOf(
                seededItems[1],
                uncompletedItem,
                completedItem
        )) {
            dataSource.put(uncompletedItem)
            dataSource.all
        }
    }

    @Test
    fun put_whenNewUri_insertsBeforeCompleted() {
        val newItem = ToDoData(uri = "todo/new", title = "another to do")

        expect(listOf(
                seededItems[0],
                seededItems[1],
                newItem,
                seededItems[2]
        )) {
            dataSource.put(newItem)
            dataSource.all
        }
    }

    @Test
    fun put_whenNewUri_andCompleted_insertsAtEnd() {
        val newItem = ToDoData(uri = "todo/new", title = "completed to do", completed = true)

        expect(listOf(
                seededItems[0],
                seededItems[1],
                seededItems[2],
                newItem
        )) {
            dataSource.put(newItem)
            dataSource.all
        }
    }

    @Test
    fun delete() {
        expect(listOf(
                seededItems[0],
                seededItems[1]
        )) {
            dataSource.delete(seededItems[2].uri)
            dataSource.all
        }
    }

    @Test
    fun delete_noOpOnMissingUri() {
        expect(seededItems) {
            dataSource.delete("non-existent")
            dataSource.all
        }
    }
}

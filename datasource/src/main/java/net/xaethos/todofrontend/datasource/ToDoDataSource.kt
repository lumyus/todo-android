package net.xaethos.todofrontend.datasource

import java.util.*

interface ToDoDataSource {

    /**
     * A list of all to do items.
     */
    val all: List<ToDoData>

    /**
     * Get item by index
     */
    operator fun get(index: Int): ToDoData

    /**
     * Find item by URI
     */
    operator fun get(uri: String): ToDoData?

    /**
     * Create a new item
     */
    fun create(title: String, details: String? = null): ToDoData

    /**
     * Insert or update an item
     */
    fun put(item: ToDoData)

    /**
     * Remove an item. Is a no-op if item is not present.
     */
    fun delete(uri: String)

}

internal class LocalSource : ToDoDataSource {

    override val all = ArrayList<ToDoData>()

    /**
     * A map of items, by URI.
     */
    private val itemMap = HashMap<String, ToDoData>()

    init {
        insertItemUnsafe(ToDoData("todo/0", "Write To Do app", completed = true))
        insertItemUnsafe(ToDoData("todo/1", "Give a talk on To Do app"))
        insertItemUnsafe(ToDoData("todo/2", "Do stuff", details = "I have stuff to do"))
        (3..30).forEach { insertItemUnsafe(ToDoData("todo/$it", "to do $it")) }
    }

    override fun get(index: Int) = all[index]

    override fun get(uri: String) = itemMap[uri]

    override fun create(title: String, details: String?): ToDoData {
        return synchronized(this) {
            val id = System.nanoTime()
            val item = ToDoData("todo/$id", title, details)
            put(item)
            item
        }
    }

    override fun put(item: ToDoData) {
        synchronized(this) {
            if (itemMap.contains(item.uri)) {
                updateItemUnsafe(item)
            } else {
                insertItemUnsafe(item)
            }
        }
    }

    override fun delete(uri: String) {
        synchronized(this) {
            if (itemMap.contains(uri)) {
                itemMap.remove(uri)
                all.removeAt(findIndex(uri))
            }
        }
    }

    /**
     * Insert a new item in correct position. Assumes item is not in source. Not thread safe.
     */
    private fun insertItemUnsafe(item: ToDoData) {
        itemMap.put(item.uri, item)
        if (item.completed) {
            all.add(item)
        } else {
            all.add(findCompletedIndex(), item)
        }
    }

    /**
     * Update an existing item. Assumes item is already in source. Not thread safe.
     */
    private fun updateItemUnsafe(item: ToDoData) {
        val key = item.uri
        val currentItem = itemMap[key]!!

        if (item != currentItem) {
            val currentIndex = findIndex(key)

            itemMap[key] = item
            if (item.completed == currentItem.completed) {
                all[currentIndex] = item
            } else {
                all.removeAt(currentIndex)
                all.add(findCompletedIndex(), item)
            }
        }
    }

    private fun findIndex(uri: String): Int {
        return all.indexOfFirst { it.uri == uri }
    }

    /**
     * Find the index where completed items would start
     */
    private fun findCompletedIndex(): Int {
        val index = all.indexOfFirst { it.completed }
        return if (index < 0) all.size else index
    }
}

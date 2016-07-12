package net.xaethos.todofrontend.datasource

import java.util.*

interface ToDoDataSource {

    /**
     * An array of all to do items.
     */
    val allItems: List<ToDoData>

    operator fun get(itemId: String): ToDoData?

}

internal object DummyContent : ToDoDataSource {

    private val COUNT = 25

    override val allItems = ArrayList<ToDoData>(COUNT)

    /**
     * A map of sample (dummy) items, by ID.
     */
    val itemMap = HashMap<String, ToDoData>(COUNT)

    init {
        (1..COUNT).map(::createDummyItem).forEach {
            allItems.add(it)
            itemMap.put(it.url, it)
        }
    }

    override fun get(itemId: String) = itemMap[itemId]
}

private fun createDummyItem(position: Int) =
        ToDoData(url = "http://example.com/todo/$position",
                title = "To do $position",
                text = makeDetails(position))

private fun makeDetails(position: Int): String {
    return with(StringBuilder()) {
        append("Details about To Do ").append(position)
        for (i in 0..position - 1) {
            append("\nMore details information here.")
        }
        toString()
    }
}

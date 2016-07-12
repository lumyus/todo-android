package net.xaethos.todofrontend.dummy

import net.xaethos.todofrontend.datasource.ToDoData
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    private val COUNT = 25

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: List<ToDoData>

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: Map<String, ToDoData>

    init {
        val list = ArrayList<ToDoData>(COUNT)
        val map = HashMap<String, ToDoData>(COUNT)

        // Add some sample items.
        for (i in 1..COUNT) {
            val item = createDummyItem(i)
            list.add(item)
            map.put(item.url, item)
        }

        ITEMS = list
        ITEM_MAP = map
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
}

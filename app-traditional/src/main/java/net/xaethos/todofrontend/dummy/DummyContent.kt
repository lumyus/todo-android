package net.xaethos.todofrontend.dummy

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
    val ITEMS: List<DummyItem>

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: Map<String, DummyItem>

    init {
        val list = ArrayList<DummyItem>(COUNT)
        val map = HashMap<String, DummyItem>(COUNT)

        // Add some sample items.
        for (i in 1..COUNT) {
            val item = createDummyItem(i)
            list.add(item)
            map.put(item.id, item)
        }

        ITEMS = list
        ITEM_MAP = map
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem(position.toString(), "Item $position", makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        return with(StringBuilder()) {
            append("Details about Item: ").append(position)
            for (i in 0..position - 1) {
                append("\nMore details information here.")
            }
            toString()
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString() = content
    }
}

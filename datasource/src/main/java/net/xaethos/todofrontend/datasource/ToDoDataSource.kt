package net.xaethos.todofrontend.datasource

import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.emptyObservable
import rx.subjects.BehaviorSubject
import java.util.*

interface ToDoDataSource {

    /**
     * A list of all to do items.
     */
    val all: Observable<List<ToDoData>>

    /**
     * Get item by index
     */
    operator fun get(index: Int): Observable<ToDoData>

    /**
     * Find item by URI
     */
    operator fun get(uri: String): Observable<ToDoData>

    /**
     * Create a new item
     */
    fun create(title: String, details: String? = null): Observable<ToDoData>

    /**
     * Insert or update an item
     */
    fun put(item: ToDoData): Observable<ToDoData>

    /**
     * Remove an item. Is a no-op if item is not present.
     */
    fun delete(uri: String)

}

internal class LocalSource : ToDoDataSource {

    private val items = ArrayList<ToDoData>()
    private val itemMap = HashMap<String, BehaviorSubject<ToDoData>>()
    private val listSubject = BehaviorSubject<List<ToDoData>>()

    override val all: Observable<List<ToDoData>> = listSubject.asObservable()

    init {
        insertItemUnsafe(ToDoData("todo/0", "Write To Do app", completed = true))
        insertItemUnsafe(ToDoData("todo/1", "Give a talk on To Do app"))
        insertItemUnsafe(ToDoData("todo/2", "Do stuff", details = "I have stuff to do"))
    }

    override fun get(index: Int) = get(items[index].uri)

    override fun get(uri: String): Observable<ToDoData> =
            itemMap[uri]?.asObservable() ?: emptyObservable()

    override fun create(title: String, details: String?): Observable<ToDoData> =
            synchronized(this) {
                val id = System.nanoTime()
                val item = ToDoData("todo/$id", title, details)
                put(item)
            }

    override fun put(item: ToDoData): Observable<ToDoData> =
            synchronized(this) {
                if (itemMap.contains(item.uri)) {
                    updateItemUnsafe(item)
                } else {
                    insertItemUnsafe(item)
                }
            }

    override fun delete(uri: String) =
            synchronized(this) {
                if (itemMap.contains(uri)) {
                    items.removeAt(findIndex(uri))
                    itemMap.remove(uri)?.onCompleted()
                    listSubject.onNext(items.toList())
                }
            }

    /**
     * Insert a new item in correct position. Assumes item is not in source. Not thread safe.
     */
    private fun insertItemUnsafe(item: ToDoData): Observable<ToDoData> {
        val itemSubject = BehaviorSubject(item)
        itemMap.put(item.uri, itemSubject)
        if (item.completed) {
            items.add(item)
        } else {
            items.add(findCompletedIndex(), item)
        }
        listSubject.onNext(items.toList())
        return itemSubject
    }

    /**
     * Update an existing item. Assumes item is already in source. Not thread safe.
     */
    private fun updateItemUnsafe(item: ToDoData): Observable<ToDoData> {
        val key = item.uri
        val itemSubject = itemMap[key] ?: throw IllegalArgumentException("Item missing: $item")
        val currentItem = itemSubject.value

        if (item != currentItem) {
            val currentIndex = findIndex(key)

            if (item.completed == currentItem.completed) {
                items[currentIndex] = item
            } else {
                items.removeAt(currentIndex)
                items.add(findCompletedIndex(), item)
            }
            itemSubject.onNext(item)
            listSubject.onNext(items.toList())
        }
        return itemSubject.asObservable()
    }

    private fun findIndex(uri: String): Int {
        return items.indexOfFirst { it.uri == uri }
    }

    /**
     * Find the index where completed items would start
     */
    private fun findCompletedIndex(): Int {
        val index = items.indexOfFirst { it.completed }
        return if (index < 0) items.size else index
    }
}

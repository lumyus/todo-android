package net.xaethos.todofrontend.datasource

import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.emptyObservable
import rx.subjects.BehaviorSubject
import java.util.*

interface TodoDataSource {

    /**
     * A list of all to do items.
     */
    val all: Observable<List<Todo>>

    /**
     * Get item by index
     */
    operator fun get(index: Int): Observable<Todo>

    /**
     * Find item by URI
     */
    operator fun get(uri: String): Observable<Todo>

    /**
     * Create a new item
     */
    fun create(title: String, details: String? = null): Observable<Todo>

    /**
     * Insert or update an item
     */
    fun put(item: Todo): Observable<Todo>

    /**
     * Remove an item. Is a no-op if item is not present.
     */
    fun delete(uri: String)

}

internal class LocalSource : TodoDataSource {

    private val items = ArrayList<Todo>()
    private val itemMap = HashMap<String, BehaviorSubject<Todo>>()
    private val listSubject = BehaviorSubject<List<Todo>>()

    override val all: Observable<List<Todo>> = listSubject.asObservable()

    init {
        insertItemUnsafe(Todo("todo/0", "Write To Do app", completed = true))
        insertItemUnsafe(Todo("todo/1", "Give a talk on To Do app"))
        insertItemUnsafe(Todo("todo/2", "Do stuff", details = "I have stuff to do"))
    }

    override fun get(index: Int) = get(items[index].uri)

    override fun get(uri: String): Observable<Todo> =
            itemMap[uri]?.asObservable() ?: emptyObservable()

    override fun create(title: String, details: String?): Observable<Todo> =
            synchronized(this) {
                val id = System.nanoTime()
                val item = Todo("todo/$id", title, details)
                put(item)
            }

    override fun put(item: Todo): Observable<Todo> =
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
    private fun insertItemUnsafe(item: Todo): Observable<Todo> {
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
    private fun updateItemUnsafe(item: Todo): Observable<Todo> {
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

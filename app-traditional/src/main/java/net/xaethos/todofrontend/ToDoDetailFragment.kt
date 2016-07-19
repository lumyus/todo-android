package net.xaethos.todofrontend

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.xaethos.todofrontend.datasource.TodoDataSource
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * A fragment representing a single to do detail screen.
 * This fragment is either contained in a [TodoListActivity]
 * in two-pane mode (on tablets) or a [TodoDetailActivity]
 * on handsets.
 */
class TodoDetailFragment : Fragment() {

    @Inject lateinit var dataSource: TodoDataSource

    private var appBarLayout: CollapsingToolbarLayout? = null
    private var subscription = Subscriptions.unsubscribed()

    private val itemId: String?
        get() = arguments.getString(ARG_ITEM_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appBarLayout = activity.findViewById(R.id.toolbar_layout) as CollapsingToolbarLayout?

        singletonComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.todo_detail, container, false)

        itemId?.let { itemId ->
            subscription = dataSource[itemId].subscribe { item ->
                appBarLayout?.title = item.title

                // Show the dummy content as text in a TextView.
                val detailView = rootView.findViewById(R.id.todo_detail) as TextView
                detailView.text = item?.details
            }
        }

        return rootView
    }

    override fun onDestroyView() {
        subscription.unsubscribe()
        super.onDestroyView()
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID = "item_id"
    }
}

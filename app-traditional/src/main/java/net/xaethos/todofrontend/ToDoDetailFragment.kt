package net.xaethos.todofrontend

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.dummy.DummyContent

/**
 * A fragment representing a single ToDo detail screen.
 * This fragment is either contained in a [ToDoListActivity]
 * in two-pane mode (on tablets) or a [ToDoDetailActivity]
 * on handsets.
 */
class ToDoDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: ToDoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments.containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            val item = DummyContent.ITEM_MAP[arguments.getString(ARG_ITEM_ID)]

            val appBarLayout = activity.findViewById(R.id.toolbar_layout) as CollapsingToolbarLayout?
            appBarLayout?.title = item?.title
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.todo_detail, container, false)

        // Show the dummy content as text in a TextView.
        val detailView = rootView.findViewById(R.id.todo_detail) as TextView
        detailView.text = item?.text

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID = "item_id"
    }
}

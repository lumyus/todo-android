package net.xaethos.todofrontend

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.datasource.TodoDataSource
import javax.inject.Inject

/**
 * An activity representing a list of to dos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TodoDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class TodoListActivity : AppCompatActivity() {

    @Inject lateinit var dataSource: TodoDataSource

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        singletonComponent.inject(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view,
                    "Replace with your own action",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        setupRecyclerView(findViewById(R.id.todo_list) as RecyclerView)

        if (findViewById(R.id.todo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = SimpleItemRecyclerViewAdapter()
        recyclerView.adapter = adapter
        dataSource.all.subscribe { adapter.values = it }
    }

    inner class SimpleItemRecyclerViewAdapter :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        var values: List<Todo> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.todo_list_content, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.uri
            holder.contentView.text = item.title

            holder.itemView.setOnClickListener { v ->
                if (twoPane) {
                    val fragment = TodoDetailFragment()
                    fragment.arguments = Bundle().apply {
                        putString(TodoDetailFragment.ARG_ITEM_ID, item.uri)
                    }
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.todo_detail_container, fragment)
                            .commit()
                } else {
                    val context = v.context
                    val intent = Intent(context, TodoDetailActivity::class.java)
                    intent.putExtra(TodoDetailFragment.ARG_ITEM_ID, item.uri)
                    context.startActivity(intent)
                }
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView
            val contentView: TextView

            init {
                idView = view.findViewById(R.id.id) as TextView
                contentView = view.findViewById(R.id.content) as TextView
            }

            override fun toString() = super.toString() + " '" + contentView.text + "'"
        }
    }
}

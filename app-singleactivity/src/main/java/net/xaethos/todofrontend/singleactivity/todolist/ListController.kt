package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.MembersInjector
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule
import javax.inject.Inject

/**
 * A controller for displaying a list of to do items.
 *
 * Controllers live on nodes in the view hierarchy. They are akin to fragments, but with a
 * simpler lifecycle. They will instantiate mediators and presenters to do the actual view
 * manipulation.
 *
 * While presenters manipulate views, controllers choose what type of view and presenter
 * will be used. Controllers are also in charge of navigation, pushing and popping new
 * controllers onto the router as the mediator requests.
 *
 * Finally, controllers handle dependency injection for mediators and presenters.
 */
class ListController() : RxController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.presenter_todo_list, container, false)

    override fun onAttach(view: View) {
        /*
        This component holds a reference to the activity, and the controller sometimes outlives
        the activity, so we create a new one every time.
        */
        val component = buildComponent()
        val presenter = component.inject(ListPresenter(view))
        component.mediator().bindListPresenter(presenter)
    }

    private fun buildComponent() = activity.component.listComponent(RxControllerModule(this))

    @ControllerScope
    class Adapter @Inject constructor(
            private val mediator: ListMediator,
            private var viewHolderInjector: MembersInjector<ListPresenter.ItemHolder>)
    : RecyclerView.Adapter<ListPresenter.ItemHolder>() {

        init {
            setHasStableIds(true)
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ListPresenter.ItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.presenter_todo_list_item, parent, false)
            val viewHolder = ListPresenter.ItemHolder(view)
            viewHolderInjector.injectMembers(viewHolder)
            return viewHolder
        }

        override fun onBindViewHolder(holder: ListPresenter.ItemHolder, position: Int) =
                mediator.bindItemPresenter(holder, position)

        override fun onViewRecycled(holder: ListPresenter.ItemHolder) = holder.onRecycle()

        override fun getItemCount() = mediator.itemCount

        override fun getItemId(position: Int): Long = mediator.itemId(position)
    }

    /**
     * The Dagger component providing dependencies for this controller's views.
     *
     * Its lifecycle should match the _view's_ lifecycle: we should create a new
     * [Component] instance on each [onCreateView].
     */
    @ControllerScope @Subcomponent(modules = arrayOf(RxControllerModule::class))
    interface Component {
        fun inject(presenter: ListPresenter): ListPresenter
        fun mediator(): ListMediator
    }
}

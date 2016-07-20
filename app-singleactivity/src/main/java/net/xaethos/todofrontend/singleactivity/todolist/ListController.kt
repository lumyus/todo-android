package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.MembersInjector
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.tododetail.DetailController
import net.xaethos.todofrontend.singleactivity.todoedit.EditController
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule
import net.xaethos.todofrontend.singleactivity.util.routerTransaction
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
class ListController() : RxController(), ListMediator.Navigator {
    val viewComponent by lazy {
        activity.component.listComponentBuilder().controllerModule(Module()).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.presenter_todo_list, container, false)

    override fun onAttach(view: View) {
        val presenter = viewComponent.inject(ListPresenter(view))
        viewComponent.mediator().bindListPresenter(presenter)
    }

    override fun pushDetailController(todo: Todo) =
            router.pushController(DetailController.create(todo.uri).routerTransaction()
                    .pushChangeHandler(HorizontalChangeHandler())
                    .popChangeHandler(HorizontalChangeHandler()))

    override fun pushCreateController() =
            router.pushController(EditController.create().routerTransaction()
                    .pushChangeHandler(VerticalChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))

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
     * `ViewComponent` instance on each `onCreateView`.
     */
    @ControllerScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(presenter: ListPresenter): ListPresenter
        fun mediator(): ListMediator

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: Module): Builder
        }
    }

    @dagger.Module
    inner class Module : RxControllerModule(this) {
        @Provides fun navigator(): ListMediator.Navigator = this@ListController
    }
}

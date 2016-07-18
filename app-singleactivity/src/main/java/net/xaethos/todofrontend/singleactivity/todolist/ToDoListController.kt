package net.xaethos.todofrontend.singleactivity.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.MembersInjector
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.CollectionScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.tododetail.ToDoDetailController
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
class ToDoListController() : RxController(), ToDoListMediator.Navigator {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewComponent = createViewComponent()
        val view = inflater.inflate(R.layout.presenter_todo_list, container, false)
        val presenter = viewComponent.inject(ToDoListPresenter(view))

        return presenter.root
    }

    override fun pushDetailController(toDo: ToDoData) =
            router.pushController(ToDoDetailController.create(toDo.uri).routerTransaction()
                    .pushChangeHandler(VerticalChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))

    private fun createViewComponent() = activity.component.toDoListComponentBuilder()
            .controllerModule(Module())
            .build()

    @CollectionScope
    class Adapter @Inject constructor(
            private val mediator: ToDoListMediator,
            private var viewHolderInjector: MembersInjector<ToDoListPresenter.ItemHolder>
    ) : RecyclerView.Adapter<ToDoListPresenter.ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ToDoListPresenter.ItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.presenter_todo_list_item, parent, false)
            val viewHolder = ToDoListPresenter.ItemHolder(view)
            viewHolderInjector.injectMembers(viewHolder)
            return viewHolder
        }

        override fun onBindViewHolder(holder: ToDoListPresenter.ItemHolder, position: Int) =
                mediator.bindItemPresenter(holder, position)

        override fun onViewRecycled(holder: ToDoListPresenter.ItemHolder) = holder.onRecycle()

        override fun getItemCount() = mediator.itemCount
    }

    /**
     * The Dagger component providing dependencies for this controller's views.
     *
     * Its lifecycle should match the _view's_ lifecycle: we should create a new
     * `ViewComponent` instance on each `onCreateView`.
     */
    @CollectionScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(presenter: ToDoListPresenter): ToDoListPresenter

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: Module): Builder
        }
    }

    @dagger.Module
    inner class Module : RxControllerModule(this) {
        @Provides fun navigator(): ToDoListMediator.Navigator = this@ToDoListController
    }
}

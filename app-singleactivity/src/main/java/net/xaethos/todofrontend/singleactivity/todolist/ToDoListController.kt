package net.xaethos.todofrontend.singleactivity.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.datasource.ToDoData
import net.xaethos.todofrontend.singleactivity.CollectionScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.tododetail.ToDoDetailController
import net.xaethos.todofrontend.singleactivity.util.routerTransaction

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
class ToDoListController() : Controller(), ToDoListMediator.Navigator {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val presenter = ToDoListPresenter(inflater.inflate(R.layout.todo_list, container, false))
        val viewComponent = activity.component.toDoListComponentBuilder()
                .controllerModule(Module())
                .build()

        return viewComponent.inject(presenter).root
    }

    override fun pushDetailController(toDo: ToDoData) {
        router.pushController(ToDoDetailController(toDo.uri).routerTransaction())
    }

    /**
     * The Dagger component providing dependencies for this controller's views.
     *
     * Its lifecycle should match the _view's_ lifecycle: we should create a new
     * `ViewComponent` instance on each `onCreateView`.
     */
    @CollectionScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(viewHolder: ToDoListPresenter): ToDoListPresenter

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: Module): Builder
        }
    }

    @dagger.Module
    inner class Module() {
        @Provides @CollectionScope fun navigator(): ToDoListMediator.Navigator = this@ToDoListController
    }
}

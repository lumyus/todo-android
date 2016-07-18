package net.xaethos.todofrontend.singleactivity.tododetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.util.DataBundle
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule

/**
 * Controller: lifecycle, navigation and dependency injection
 */
class ToDoDetailController(val args: Arguments) : RxController(args.bundle) {

    @Suppress("unused")
    constructor(bundle: Bundle) : this(Arguments(bundle))

    companion object {
        fun create(uri: String): ToDoDetailController {
            val args = ToDoDetailController.Arguments(Bundle()).apply {
                this.uri = uri
            }
            return ToDoDetailController(args)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewComponent = createViewComponent()
        val view = inflater.inflate(R.layout.todo_detail, container, false)
        val presenter = viewComponent.inject(ToDoDetailPresenter(view))
        val mediator = viewComponent.mediator()

        mediator.bindPresenter(presenter, args.uri!!)
        return presenter.root
    }

    private fun createViewComponent() = activity.component.toDoDetailComponentBuilder()
            .controllerModule(RxControllerModule(this))
            .build()

    class Arguments(bundle: Bundle) : DataBundle(bundle) {
        var uri by bundleString
    }

    @ItemScope @Subcomponent(modules = arrayOf(RxControllerModule::class))
    interface ViewComponent {
        fun inject(viewHolder: ToDoDetailPresenter): ToDoDetailPresenter
        fun mediator(): ToDoDetailMediator

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: RxControllerModule): Builder
        }
    }
}

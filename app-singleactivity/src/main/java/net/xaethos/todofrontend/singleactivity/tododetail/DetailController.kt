package net.xaethos.todofrontend.singleactivity.tododetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.todoedit.EditController
import net.xaethos.todofrontend.singleactivity.util.DataBundle
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule
import net.xaethos.todofrontend.singleactivity.util.routerTransaction

/**
 * Controller: lifecycle, navigation and dependency injection
 */
class DetailController(val args: Arguments) : RxController(args.bundle), DetailMediator.Navigator {

    @Suppress("unused")
    constructor(bundle: Bundle) : this(Arguments(bundle))

    companion object {
        fun create(uri: String): DetailController {
            val args = DetailController.Arguments(Bundle()).apply {
                this.uri = uri
            }
            return DetailController(args)
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    val viewComponent by lazy {
        activity.component.detailComponentBuilder().controllerModule(Module()).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.presenter_todo_detail, container, false)

    override fun onAttach(view: View) {
        val presenter = viewComponent.inject(DetailPresenter(view))
        viewComponent.mediator().bindPresenter(presenter, args.uri!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> router.popCurrentController()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun pushEditController(todo: Todo) =
            router.pushController(EditController.create(todo.uri).routerTransaction()
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))

    class Arguments(bundle: Bundle) : DataBundle(bundle) {
        var uri by bundleString
    }

    @ControllerScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(viewHolder: DetailPresenter): DetailPresenter
        fun mediator(): DetailMediator

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: Module): Builder
        }
    }

    @dagger.Module
    inner class Module : RxControllerModule(this) {
        @Provides fun navigator(): DetailMediator.Navigator = this@DetailController
    }
}

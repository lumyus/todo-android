package net.xaethos.todofrontend.singleactivity.tododetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
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
class DetailController(val args: Arguments) : RxController(args.bundle) {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewComponent = createViewComponent()
        val view = inflater.inflate(R.layout.presenter_todo_detail, container, false)
        val presenter = viewComponent.inject(DetailPresenter(view))
        val mediator = viewComponent.mediator()

        mediator.bindPresenter(presenter, args.uri!!)
        return presenter.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> router.popCurrentController()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun createViewComponent() = activity.component.detailComponentBuilder()
            .controllerModule(RxControllerModule(this))
            .build()

    class Arguments(bundle: Bundle) : DataBundle(bundle) {
        var uri by bundleString
    }

    @ItemScope @Subcomponent(modules = arrayOf(RxControllerModule::class))
    interface ViewComponent {
        fun inject(viewHolder: DetailPresenter): DetailPresenter
        fun mediator(): DetailMediator

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: RxControllerModule): Builder
        }
    }
}

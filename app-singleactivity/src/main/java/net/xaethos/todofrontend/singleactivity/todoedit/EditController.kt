package net.xaethos.todofrontend.singleactivity.todoedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.ControllerScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.util.DataBundle
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule

/**
 * Controller: lifecycle, navigation and dependency injection
 */
class EditController(val args: Arguments) : RxController(args.bundle), EditMediator.Navigator {

    @Suppress("unused")
    constructor(bundle: Bundle) : this(Arguments(bundle))

    companion object {
        fun create(uri: String? = null): EditController {
            val args = EditController.Arguments(Bundle()).apply {
                this.uri = uri
            }
            return EditController(args)
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.presenter_todo_edit, container, false)

    override fun onAttach(view: View) {
        val component = buildComponent()
        val presenter = component.inject(EditPresenter(view))
        component.mediator().bindPresenter(presenter, args.uri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> router.popCurrentController()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun navigateBack() {
        router.popCurrentController()
    }

    fun buildComponent() = activity.component.editComponent(Module())

    class Arguments(bundle: Bundle) : DataBundle(bundle) {
        var uri by bundleString
    }

    @ControllerScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(viewHolder: EditPresenter): EditPresenter
        fun mediator(): EditMediator
    }

    @dagger.Module
    inner class Module : RxControllerModule(this) {
        @Provides fun navigator(): EditMediator.Navigator = this@EditController
    }
}

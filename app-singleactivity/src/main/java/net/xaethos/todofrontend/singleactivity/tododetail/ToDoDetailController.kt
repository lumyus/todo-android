package net.xaethos.todofrontend.singleactivity.tododetail

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.ItemScope
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.component
import net.xaethos.todofrontend.singleactivity.util.*
import javax.inject.Inject

/**
 * View presenter: UI controls and events
 */
class ToDoDetailController(bundle: Bundle) : Controller(bundle) {

    constructor(uri: String) : this(bundleData(::Arguments) {
        this.uri = uri
    })

    val args = Arguments(bundle)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val viewHolder = ViewHolder(inflater.inflate(R.layout.todo_detail, container, false))
        val viewComponent = activity.component.toDoDetailComponentBuilder()
                .controllerModule(Module(viewHolder, args.uri!!))
                .build()

        return viewComponent.inject(viewHolder).root
    }

    class Arguments(bundle: Bundle) : DataBundle(bundle) {
        var uri by stringDelegate
    }

    class ViewHolder(override val root: View) : ControllerViewHolder, ToDoDetailMediator.Presenter {
        @Inject lateinit var mediator: ToDoDetailMediator

        private val appBarLayout: CollapsingToolbarLayout by bindView(R.id.toolbar_layout)
        private val toolbar: Toolbar by bindView(R.id.detail_toolbar)
        private val detailView: TextView by bindView(R.id.todo_detail)

        override var titleText: CharSequence?
            get() = appBarLayout.title
            set(value) {
                appBarLayout.title = value
            }

        override var detailsText by textViewText(detailView)
    }

    @ItemScope @Subcomponent(modules = arrayOf(Module::class))
    interface ViewComponent {
        fun inject(viewHolder: ViewHolder): ViewHolder

        @Subcomponent.Builder
        interface Builder {
            fun build(): ViewComponent
            fun controllerModule(module: Module): Builder
        }
    }

    @dagger.Module
    class Module(val viewHolder: ViewHolder, val uri: String) {
        @Provides @ItemScope fun presenter(): ToDoDetailMediator.Presenter = viewHolder
        @Provides fun uri() = uri
    }
}

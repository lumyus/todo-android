package net.xaethos.todofrontend.singleactivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.singleactivity.tododetail.DetailController
import net.xaethos.todofrontend.singleactivity.tododetail.DetailMediator
import net.xaethos.todofrontend.singleactivity.todoedit.EditController
import net.xaethos.todofrontend.singleactivity.todoedit.EditMediator
import net.xaethos.todofrontend.singleactivity.todolist.ListController
import net.xaethos.todofrontend.singleactivity.todolist.ListMediator
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule
import net.xaethos.todofrontend.singleactivity.util.bindView
import net.xaethos.todofrontend.singleactivity.util.routerTransaction

class SingleActivity : AppCompatActivity(), ListMediator.Navigator, DetailMediator.Navigator, EditMediator.Navigator {

    val container by bindView<ViewGroup>(android.R.id.content)

    lateinit var router: Router
    lateinit var component: Component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = singletonComponent.activityComponent(Module())

        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(ListController().routerTransaction())
        }
    }

    override fun pushDetailController(todo: Todo) =
            router.pushController(DetailController.create(todo.uri).routerTransaction()
                    .pushChangeHandler(HorizontalChangeHandler())
                    .popChangeHandler(HorizontalChangeHandler()))

    override fun pushCreateController() =
            router.pushController(EditController.create().routerTransaction()
                    .pushChangeHandler(VerticalChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))

    override fun pushEditController(todo: Todo) =
            router.pushController(EditController.create(todo.uri).routerTransaction()
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))

    override fun navigateBack() {
        router.popCurrentController()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }

    @ActivityScope @Subcomponent(modules = arrayOf(Module::class))
    interface Component {
        fun listComponent(module: RxControllerModule): ListController.Component
        fun detailComponent(module: RxControllerModule): DetailController.Component
        fun editComponent(module: RxControllerModule): EditController.Component
    }

    @dagger.Module
    inner class Module {
        @Provides @ActivityScope fun context(): Context = this@SingleActivity
        @Provides @ActivityScope fun activity(): SingleActivity = this@SingleActivity

        @Provides fun listNavigator(): ListMediator.Navigator = this@SingleActivity
        @Provides fun detailNavigator(): DetailMediator.Navigator = this@SingleActivity
        @Provides fun editNavigator(): EditMediator.Navigator = this@SingleActivity
    }
}

val Activity.component: SingleActivity.Component
    get() = if (this is SingleActivity) component else throw IllegalStateException("Somebody set up us the bomb")

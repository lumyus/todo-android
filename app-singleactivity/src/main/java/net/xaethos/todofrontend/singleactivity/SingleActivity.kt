package net.xaethos.todofrontend.singleactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Router
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.todolist.ToDoListController
import net.xaethos.todofrontend.singleactivity.util.routerTransaction
import javax.inject.Inject

class SingleActivity : AppCompatActivity() {

    @Inject lateinit var router: Router

    lateinit var component: Component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        val module = ActivityModule(this, savedInstanceState)
        component = singletonComponent.activityComponent(module)
        component.inject(this)

        if (!router.hasRootController()) {
            val controller = component.toDoListSubcomponent().createController()
            router.setRoot(controller.routerTransaction())
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }

    @ActivityScope @Subcomponent(modules = arrayOf(ActivityModule::class))
    interface Component {
        fun inject(activity: SingleActivity)

        fun toDoListSubcomponent(): ToDoListController.Component
    }
}

package net.xaethos.todofrontend.singleactivity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import dagger.Provides
import dagger.Subcomponent
import net.xaethos.todofrontend.singleactivity.tododetail.DetailController
import net.xaethos.todofrontend.singleactivity.tododetail.DetailMediator
import net.xaethos.todofrontend.singleactivity.todoedit.EditController
import net.xaethos.todofrontend.singleactivity.todoedit.EditMediator
import net.xaethos.todofrontend.singleactivity.todolist.ListController
import net.xaethos.todofrontend.singleactivity.todolist.ListMediator
import net.xaethos.todofrontend.singleactivity.util.RxControllerModule
import net.xaethos.todofrontend.singleactivity.util.bindView
import rx.lang.kotlin.PublishSubject

class MainActivity : AppCompatActivity() {

    val coordinatorLayout by bindView<CoordinatorLayout>(R.id.coordinator)

    lateinit var dependencySource: Component

    private val destroys = PublishSubject<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = NavigationPresenter(coordinatorLayout, destroys.asObservable())
        dependencySource = singletonComponent.activityComponent(Module(presenter))
        dependencySource.mediator().bindPresenter(this, presenter, savedInstanceState)
    }

    override fun onDestroy() {
        destroys.onNext(Unit)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (!dependencySource.mediator().handleBack()) super.onBackPressed()
    }

    @ActivityScope @Subcomponent(modules = arrayOf(Module::class))
    interface Component {
        fun mediator(): NavigationMediator

        fun listComponent(module: RxControllerModule): ListController.Component
        fun detailComponent(module: RxControllerModule): DetailController.Component
        fun editComponent(module: RxControllerModule): EditController.Component
    }

    @dagger.Module
    inner class Module(val presenter: NavigationPresenter) {
        @Provides @ActivityScope fun context(): Context = this@MainActivity
        @Provides @ActivityScope fun activity(): MainActivity = this@MainActivity

        @Provides fun navigationPresenter() = presenter

        @Provides fun listNavigator(mediator: NavigationMediator): ListMediator.Navigator = mediator
        @Provides fun detailNavigator(mediator: NavigationMediator): DetailMediator.Navigator = mediator
        @Provides fun editNavigator(mediator: NavigationMediator): EditMediator.Navigator = mediator
    }
}

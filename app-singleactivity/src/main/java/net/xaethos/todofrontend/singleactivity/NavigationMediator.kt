package net.xaethos.todofrontend.singleactivity

import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import net.xaethos.todofrontend.datasource.Todo
import net.xaethos.todofrontend.singleactivity.tododetail.DetailController
import net.xaethos.todofrontend.singleactivity.tododetail.DetailMediator
import net.xaethos.todofrontend.singleactivity.todoedit.EditController
import net.xaethos.todofrontend.singleactivity.todoedit.EditMediator
import net.xaethos.todofrontend.singleactivity.todolist.ListController
import net.xaethos.todofrontend.singleactivity.todolist.ListMediator
import net.xaethos.todofrontend.singleactivity.util.pushController
import javax.inject.Inject

@ActivityScope
class NavigationMediator @Inject constructor() :
        ListMediator.Navigator, DetailMediator.Navigator, EditMediator.Navigator {
    lateinit var router: Router

    fun bindPresenter(activity: MainActivity, presenter: NavigationPresenter, savedState: Bundle?) {
        router = Conductor.attachRouter(activity, presenter.container, savedState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ListController()))
        }
    }

    fun handleBack(): Boolean = router.handleBack()

    override fun pushDetailController(todo: Todo) {
        router.pushController(DetailController.create(todo.uri))
    }

    override fun pushCreateController() {
        router.pushController(EditController.create())
    }

    override fun pushEditController(todo: Todo) {
        router.pushController(EditController.create(todo.uri))
    }

    override fun navigateBack() {
        router.popCurrentController()
    }
}

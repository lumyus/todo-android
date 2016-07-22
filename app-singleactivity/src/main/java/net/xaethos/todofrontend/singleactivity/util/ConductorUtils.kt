package net.xaethos.todofrontend.singleactivity.util

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.Module
import dagger.Provides
import rx.Observable

@Module
open class RxControllerModule(val controller: RxController) {
    @Provides fun controllerDetachesObservable(): Observable<Unit> {
        return controller.lifecycle()
                .filter { it == ControllerEvent.DETACH }
                .map { Unit }
    }
}

inline fun Router.pushController(controller: Controller, init: RouterTransaction.() -> Unit) {
    val transaction = RouterTransaction.with(controller)
    transaction.init()
    pushController(transaction)
}

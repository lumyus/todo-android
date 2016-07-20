package net.xaethos.todofrontend.singleactivity.util

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent
import com.bluelinelabs.conductor.rxlifecycle.RxController
import dagger.Module
import dagger.Provides
import rx.Observable

fun Controller.routerTransaction(): RouterTransaction = RouterTransaction.with(this)

@Module
open class RxControllerModule(val controller: RxController) {
    @Provides fun controllerDetachesObservable(): Observable<Unit> {
        return controller.lifecycle()
                .filter { it == ControllerEvent.DETACH }
                .map { Unit }
    }
}

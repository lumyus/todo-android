package net.xaethos.todofrontend.singleactivity.util

import android.support.v7.widget.RecyclerView
import android.view.View
import rx.Observable
import rx.lang.kotlin.PublishSubject

interface Presenter {
    val root: View

    val unbinds: Observable<Unit>
}

abstract class ViewHolderPresenter(override val root: View) : RecyclerView.ViewHolder(root), Presenter {

    abstract val controllerUnbinds: Observable<Unit>

    override val unbinds: Observable<Unit>
        get() = controllerUnbinds.mergeWith(recycleSubject.asObservable()).first()

    private val recycleSubject = PublishSubject<Unit>()

    fun onRecycle() = recycleSubject.onNext(Unit)

}

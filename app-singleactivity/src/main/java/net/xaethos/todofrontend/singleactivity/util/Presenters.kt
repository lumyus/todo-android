package net.xaethos.todofrontend.singleactivity.util

import android.support.v7.widget.RecyclerView
import android.view.View
import rx.Observable
import rx.lang.kotlin.PublishSubject

interface ViewPresenter {
    val root: View

    val detaches: Observable<Unit>
}

abstract class ViewHolderPresenter(override val root: View) : RecyclerView.ViewHolder(root), ViewPresenter {

    abstract val controllerDetaches: Observable<Unit>

    override val detaches: Observable<Unit>
        get() = controllerDetaches.mergeWith(recycleSubject.asObservable()).first()

    private val recycleSubject = PublishSubject<Unit>()

    fun onRecycle() = recycleSubject.onNext(Unit)

}

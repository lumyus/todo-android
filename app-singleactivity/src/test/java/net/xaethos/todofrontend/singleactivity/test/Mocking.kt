package net.xaethos.todofrontend.singleactivity.test

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import rx.Observable
import rx.subjects.Subject
import kotlin.test.assertTrue

inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

inline fun <reified T : Any> mock(initialization: T.() -> Unit): T =
        Mockito.mock(T::class.java).apply(initialization)

fun <T> stub(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

inline fun <R> OngoingStubbing<R>.with(crossinline answer: () -> R): OngoingStubbing<R> =
        then { invocation -> answer() }

inline fun <reified P0 : Any, R> OngoingStubbing<R>.with(crossinline answer: (P0) -> R): OngoingStubbing<R> =
        then { invocation ->
            assertTrue("invocation has enough parameters") { invocation.arguments.size >= 1 }
            answer(
                    invocation.getArgumentAt(0, P0::class.java)
            )
        }

fun <T> OngoingStubbing<Observable<T>>.withSubject(subject: Subject<T, T>): Subject<T, T> {
    thenReturn(subject.asObservable())
    return subject
}

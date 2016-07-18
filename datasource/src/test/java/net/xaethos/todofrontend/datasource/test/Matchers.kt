package net.xaethos.todofrontend.datasource.test

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import rx.Observable
import rx.observers.TestSubscriber
import java.util.concurrent.TimeUnit

infix fun <T> T.shouldEqual(expected: T) {
    assertThat(this, equalTo(expected))
}

inline fun <T> Observable<T>.withTestSubscriber(body: (TestSubscriber<T>) -> Unit) {
    val testSubscriber = TestSubscriber<T>()
    subscribe(testSubscriber)
    body(testSubscriber)
}

fun <T> emits(vararg expected: T) = object : Matcher<Observable<T>> {
    override val description = "was observable that emits: $expected"

    override fun invoke(actual: Observable<T>): MatchResult {
        val testSubscriber = TestSubscriber<T>()
        actual.subscribe(testSubscriber)
        testSubscriber.awaitValueCount(expected.size, 300, TimeUnit.MILLISECONDS)
        return nextEventsMatchResult(expected, testSubscriber.onNextEvents)
    }

}

fun <T> onNextEvents(vararg expected: T): Matcher<TestSubscriber<T>> = NextEventsMatcher(expected)

private class NextEventsMatcher<T>(val expected: Array<out T>) : Matcher<TestSubscriber<T>> {
    override val description = "was TestSubscriber with onNext events: $expected"

    override fun invoke(actual: TestSubscriber<T>) =
            nextEventsMatchResult(expected, actual.onNextEvents)
}

private fun <T> nextEventsMatchResult(expected: Array<out T>, actual: List<T>): MatchResult {
    if (expected.size != actual.size) {
        return MatchResult.Mismatch("${expected.size} events instead of ${actual.size}")
    }
    (0..expected.size - 1).forEach { index ->
        if (expected[index] != actual[index]) {
            return MatchResult.Mismatch("event $index was $actual")
        }
    }
    return MatchResult.Match
}

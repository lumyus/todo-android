package net.xaethos.todofrontend.singleactivity.test

import org.mockito.Mockito

inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

inline fun <reified T : Any> mock(initialization: T.() -> Unit): T =
        Mockito.mock(T::class.java).apply(initialization)

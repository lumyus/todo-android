package net.xaethos.todofrontend.singleactivity

import android.app.Activity
import dagger.Component
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Scope
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope @Retention(RUNTIME) annotation class ActivityScope
@Scope @Retention(RUNTIME) annotation class ControllerScope

@Singleton @Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun activityComponent(module: MainActivity.Module): MainActivity.Component
}

val singletonComponent: SingletonComponent = DaggerSingletonComponent.create()

val Activity.component: MainActivity.Component
    get() = if (this is MainActivity) dependencySource else throw IllegalStateException("Somebody set up us the bomb")

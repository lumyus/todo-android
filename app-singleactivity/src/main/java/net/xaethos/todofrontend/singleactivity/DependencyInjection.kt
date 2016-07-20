package net.xaethos.todofrontend.singleactivity

import dagger.Component
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Scope
import javax.inject.Singleton

@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ActivityScope
@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ControllerScope

@Singleton @Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun activityComponent(module: SingleActivity.Module): SingleActivity.Component
}

val singletonComponent: SingletonComponent = DaggerSingletonComponent.create()

package net.xaethos.todofrontend.singleactivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import dagger.Component
import dagger.Module
import dagger.Provides
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Scope
import javax.inject.Singleton

//region Scopes -- dependency scopes available to the application

@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ActivityScope

@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ToDoListScope
//endregion


//region Modules -- generic modules that can be used by several component

@Module
class ActivityModule(private val activity: Activity, private val savedInstanceState: Bundle?) {
    @Provides @ActivityScope fun context(): Context = activity

    @Provides @ActivityScope fun router(): Router {
        val container = activity.findViewById(R.id.controller_container) as ViewGroup
        return Conductor.attachRouter(activity, container, savedInstanceState)
    }
}
//endregion

//region SingletonComponent -- the root component for the application

@Singleton @Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun activityComponent(module: ActivityModule): SingleActivity.Component
}

val singletonComponent: SingletonComponent = DaggerSingletonComponent.create()
//endregion

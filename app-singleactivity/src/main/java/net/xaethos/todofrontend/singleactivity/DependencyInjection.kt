package net.xaethos.todofrontend.singleactivity

import android.app.Activity
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Scope
import javax.inject.Singleton

@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ActivityScope
@Scope @Retention(AnnotationRetention.RUNTIME) annotation class CollectionScope
@Scope @Retention(AnnotationRetention.RUNTIME) annotation class ItemScope

@Singleton @Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun activityComponent(module: ActivityModule): SingleActivity.Component

}

val singletonComponent: SingletonComponent = DaggerSingletonComponent.create()

@Module
class ActivityModule(private val activity: Activity) {
    @Provides @ActivityScope fun context(): Context = activity
}

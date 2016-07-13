package net.xaethos.todofrontend.singleactivity.todolist

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ToDoListScope

@ToDoListScope
@Subcomponent(modules = arrayOf(ToDoListModule::class))
interface ToDoListComponent {
    fun inject(controller: ToDoListController)
}

@Module
class ToDoListModule(
        @get:Provides @ToDoListScope val context: Context,
        @get:Provides @ToDoListScope val listPresenter: ToDoListMediator.ListPresenter
)

package net.xaethos.todofrontend.singleactivity

import dagger.Component
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun toDoListComponent(module: ToDoListModule): ToDoListComponent
}

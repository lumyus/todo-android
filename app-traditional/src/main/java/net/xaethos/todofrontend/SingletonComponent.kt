package net.xaethos.todofrontend

import dagger.Component
import net.xaethos.todofrontend.datasource.DataModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class))
interface SingletonComponent {
    fun inject(activity: ToDoListActivity)

    fun inject(fragment: ToDoDetailFragment)
}

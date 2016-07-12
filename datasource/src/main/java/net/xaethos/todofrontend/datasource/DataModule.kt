package net.xaethos.todofrontend.datasource

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton
    fun dataSource(): ToDoDataSource = DummyContent

}

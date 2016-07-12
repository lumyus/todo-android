package net.xaethos.todofrontend.datasource

data class ToDoData(
        val url: String,
        val title: String,
        val text: String? = null,
        val completed: Boolean = false,
        val order: Int = 0)

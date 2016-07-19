package net.xaethos.todofrontend.datasource

data class Todo(
        val uri: String,
        val title: String,
        val details: String? = null,
        val completed: Boolean = false)

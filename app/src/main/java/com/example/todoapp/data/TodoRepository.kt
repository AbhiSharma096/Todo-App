package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {

      suspend fun insert(todo: Todo)

      suspend fun delete(todo: Todo)

      suspend fun getTodoById(id: Int): Todo?

      fun getTodos(): Flow<List<Todo>>
}
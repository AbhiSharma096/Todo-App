package com.example.todoapp.ui.theme.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.util.Routes
import com.example.todoapp.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TodoListViewModel @Inject constructor(
      private val repository: TodoRepository
) : ViewModel() {

      val todos = repository.getTodos()

      private val _uiEvents = Channel<UiEvents>()
      val uiEvents =  _uiEvents.receiveAsFlow()

      private var deletedTodo: Todo? = null

      fun onEvent(event: TodoListEvent) {
            when(event) {
                  is TodoListEvent.OnTodoClick -> {
                        sendUiEvent(UiEvents.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
                  }
                  is TodoListEvent.OnAddTodoClick -> {
                        sendUiEvent(UiEvents.Navigate(Routes.ADD_EDIT_TODO))
                  }
                  is TodoListEvent.OnUndoDeleteClick -> {
                        deletedTodo?.let { todo ->
                              viewModelScope.launch {
                                    repository.insert(todo)
                              }
                        }
                  }
                  is TodoListEvent.OnDeleteTodoClick -> {
                        viewModelScope.launch {
                              deletedTodo = event.todo
                              repository.delete(event.todo)
                              sendUiEvent(UiEvents.ShowSnackbar(
                                    message = "Todo deleted",
                                    action = "Undo"
                              ))
                        }
                  }
                  is TodoListEvent.OnDoneChange -> {
                        viewModelScope.launch {
                              repository.insert (
                                    event.todo.copy(
                                          isDone = event.isDone
                                    )
                              )
                        }
                  }
            }
      }

      private fun sendUiEvent(event: UiEvents) {
            viewModelScope.launch {
                  _uiEvents.send(event)
            }
      }
}
package com.example.todoapp.ui.theme.add.edit.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
      private val repository: TodoRepository,
      savedStateHandle: SavedStateHandle
): ViewModel() {
      var todo by mutableStateOf<Todo?>(null)
            private set

      var title by mutableStateOf("")
            private set

      var description by mutableStateOf("")
            private set

      private val _uiEvents = Channel<UiEvents>()
      val uiEvents =  _uiEvents.receiveAsFlow()

      init {
            val todoId = savedStateHandle.get<Int>("todoId")!!
            if (todoId != -1) {
                  viewModelScope.launch {
                        repository.getTodoById(todoId)?.let { todo ->
                              title = todo.tittle
                              description = todo.description ?: ""
                              this@AddEditTodoViewModel.todo = todo
                        }
                  }
            }
      }


      fun onEvent(event: AddEditTodoEvent){
            when (event) {
                  is AddEditTodoEvent.OnTitleChange -> {
                        title = event.title
                  }

                  is AddEditTodoEvent.OnDescriptionChange -> {
                        description = event.description
                  }

                  is AddEditTodoEvent.OnSaveTodoClick -> {
                        viewModelScope.launch {
                              if (title.isBlank()) {
                                    sendUiEvent(
                                          UiEvents.ShowSnackbar(
                                                message = "The title can't be empty"
                                          )
                                    )
                                    return@launch
                              }
                              repository.insert(
                                    Todo(
                                          tittle = title,
                                          description = description,
                                          isDone = todo?.isDone ?: false,
                                          id = todo?.id
                                    )
                              )
                              sendUiEvent(UiEvents.PopBackStack)
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

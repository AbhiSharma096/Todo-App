package com.example.todoapp.ui.theme.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.util.UiEvents
import kotlinx.coroutines.flow.collect


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
      onNavigate: (UiEvents.Navigate) -> Unit,
      viewModel: TodoListViewModel = hiltViewModel()
) {
      val todos = viewModel.todos.collectAsState(initial = emptyList())
      val scaffoldState = remember {
            SnackbarHostState()
      }
      LaunchedEffect(key1 = true) {
            viewModel.uiEvents.collect { event ->
                  when (event) {
                        is UiEvents.ShowSnackbar -> {
                              val result = scaffoldState.showSnackbar(
                                    message = event.message,
                                    actionLabel = event.action
                              )
                              if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                              }
                        }

                        is UiEvents.Navigate -> onNavigate(event)
                        else -> Unit
                  }
            }
      }
      Scaffold(
            snackbarHost = {
                  SnackbarHost(
                        hostState = scaffoldState,
                        modifier = Modifier.padding(16.dp)
                  )
            },
            floatingActionButton = {
                  FloatingActionButton(onClick = {
                        viewModel.onEvent(TodoListEvent.OnAddTodoClick)
                  }) {
                        Icon(
                              imageVector = Icons.Default.Add,
                              contentDescription = "Add"
                        )
                  }
            }
      ) {
            LazyColumn(
                  modifier = Modifier.fillMaxSize()
            ) {
                  items(todos.value) { todo ->
                        TodoItem(
                              todo = todo,
                              onEvents = viewModel::onEvent,
                              modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                          viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                    }
                                    .padding(16.dp)
                        )
                  }
            }
      }

}
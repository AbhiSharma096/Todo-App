package com.example.todoapp.ui.theme.add.edit.todo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.util.UiEvents

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditTodoScreen(
      onPopBackScreen: () -> Unit,
      viewModel: AddEditTodoViewModel = hiltViewModel()
) {

      val scaffoldState = remember{
                SnackbarHostState()
      }
      LaunchedEffect(key1 = true) {
            viewModel.uiEvents.collect { event ->
                  when(event) {
                        is UiEvents.PopBackStack -> onPopBackScreen()
                        is UiEvents.ShowSnackbar -> {
                              scaffoldState.showSnackbar(
                                    message = event.message,
                                    actionLabel = event.action
                              )
                        }
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
            modifier = Modifier
                  .fillMaxSize()
                  .padding(16.dp),
            floatingActionButton = {
                  FloatingActionButton(onClick = {
                        viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)
                  }) {
                        Icon(
                              imageVector = Icons.Default.Check,
                              contentDescription = "Save"
                        )
                  }
            }
      ){
                Column(
                        modifier = Modifier
                         .fillMaxSize()
                         .padding(16.dp)
                ) {
                        TextField(
                         value = viewModel.title,
                         onValueChange = {
                                   viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                         },
                         label = {
                                   Text(text = "Title")
                         },
                         modifier = Modifier
                                   .fillMaxWidth()
                                   .height(56.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                         value = viewModel.description,
                         onValueChange = {
                                   viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                         },
                         label = {
                                   Text(text = "Description")
                         },
                         modifier = Modifier
                                   .fillMaxWidth()
                                   .height(56.dp)
                        )
                }
      }
}
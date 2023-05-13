package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.ui.theme.TODOAppTheme
import com.example.todoapp.ui.theme.add.edit.todo.AddEditTodoScreen
import com.example.todoapp.ui.theme.todo_list.TodoListScreen
import com.example.todoapp.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {

                  TODOAppTheme {
                        val navController = rememberNavController()
                        NavHost(
                              navController = navController, startDestination = Routes.TODO_LIST
                        ) {
                              composable(route = Routes.TODO_LIST) {
                                    TodoListScreen(
                                          onNavigate = { route ->
                                                navController.navigate(route.route)
                                          }
                                    )
                              }
                              composable(route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                                    arguments = listOf(
                                          navArgument(name = "todoId") {
                                                type = NavType.IntType
                                                defaultValue = -1
                                          }
                                    )
                              ) {
                                    AddEditTodoScreen(onPopBackScreen ={
                                          navController.popBackStack()
                                    } )

                              }

                        }

                  }

            }
      }
}








package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.data.local.AppDatabase
import com.example.data.repository.KushalRepository
import com.example.ui.navigation.KushalNavGraph
import com.example.ui.theme.KushalStoreTheme
import com.example.viewmodel.KushalViewModel
import com.example.viewmodel.KushalViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: KushalViewModel by viewModels {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = KushalRepository(database.kushalDao())
        KushalViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KushalStoreTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    KushalNavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

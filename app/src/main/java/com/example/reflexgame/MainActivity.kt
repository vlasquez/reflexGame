package com.example.reflexgame

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReflexMainScreen()
        }
    }

    @Composable
    private fun ReflexMainScreen(reflexViewModel: ReflexGameViewModel = viewModel()) {
        val reflexState by reflexViewModel.viewModelState.collectAsState()

        Column {
            reflexState.reflextButtonIndex?.let {
                if(reflexState.isGameStarted) {
                    ButtonGrid(it, reflexState.cleanBoard) { isReflexButton ->
                        reflexViewModel.pointCounter(isReflexButton = isReflexButton)
                    }
                }
            }
            InfoSection(reflexState.currentPoints)
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                onClick = {
                    reflexViewModel.startGame()
                }) {
                Text(
                    text = if (reflexState.isGameStarted) stringResource(id = R.string.stop_game)
                    else stringResource(id = R.string.start_game)
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ButtonGrid(
        reflexButtonIndex: Int,
        cleanBoard: Boolean,
        onGridButtonClick: (isReflexButton: Boolean) -> Unit
    ) {
        LazyVerticalGrid(cells = GridCells.Fixed(4)) {
            items(16) { index ->
                OutlinedButton(
                    modifier = Modifier.size(100.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (index == reflexButtonIndex && !cleanBoard) Color.Blue else Color.White
                    ),
                    onClick = { onGridButtonClick(index == reflexButtonIndex) },
                    content = {})
            }
        }
    }

    @Composable
    private fun InfoSection(currentPoints: Int = 1) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.current_points, currentPoints))
            Text(text = stringResource(id = R.string.elapsed_time))
        }
    }

    @Composable
    @Preview
    private fun ReflexGamePreview() {
        ReflexMainScreen()
    }
}
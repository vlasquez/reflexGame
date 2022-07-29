package com.example.reflexgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReflexGameViewModel : ViewModel() {
    private var turnCounter = 0
    private var job: Job? = null

    private var realTime: Long = INITIAL_TIME.toLong()

    private val _viewModelState = MutableStateFlow(ReflexViewModelState(isLoading = true))
    val viewModelState: StateFlow<ReflexViewModelState> = _viewModelState

    fun startGame() {
        if (!_viewModelState.value.isGameStarted) {
            job = viewModelScope.launch {
                _viewModelState.update { it.copy(isGameStarted = true, cleanBoard = false) }
                while (true) {
                    if (turnCounter == TURNS_PER_LEVEL) {
                        turnCounter = 0
                        realTime *= LEVEL_MULTIPLIER.toLong()
                    }
                    getReflexButtons()
                    delay(realTime)
                }
            }
        } else {
            stopGame()
        }
    }

    private fun startTimer() {

    }

    fun pointCounter(isReflexButton: Boolean) {
        viewModelScope.launch {
            _viewModelState.update {
                it.copy(
                    currentPoints = if (isReflexButton) {
                        _viewModelState.value.currentPoints + 1
                    } else {
                        _viewModelState.value.currentPoints - 1
                    }
                )
            }
            if (viewModelState.value.currentPoints == 0) stopGame()
        }
    }

    private fun getReflexButtons() {
        val reflexItemIndex = (ITEM_LIMIT).random()
        _viewModelState.update { it.copy(reflextButtonIndex = reflexItemIndex) }
    }

    private fun stopGame() {
        _viewModelState.update { it.copy(currentPoints = 1, isGameStarted = false, cleanBoard = true) }
        job?.cancel()
        job = null

    }

    companion object {
        val ITEM_LIMIT = 0..15
        const val INITIAL_TIME = 3000
        const val TURNS_PER_LEVEL = 5
        const val LEVEL_MULTIPLIER = 0.9
    }

}

data class ReflexViewModelState(
    val isLoading: Boolean = false,
    val isGameStarted: Boolean = false,
    val reflextButtonIndex: Int? = null,
    val cleanBoard: Boolean = false,
    val currentPoints: Int = 1
)
package com.example.learningproject.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class MainVM :ViewModel() {
    private val actionChannel=Channel<MainAction>(Channel.BUFFERED)
    val stateFlow: StateFlow<MainState>
    suspend fun process(action: MainAction)=actionChannel.send(action)
    init{
        val initial=MainState()
        stateFlow= MutableStateFlow(initial)
        var resume=0L
        actionChannel
            .consumeAsFlow()
            .onEach {
                action->
                when(action)
                {
                   MainAction.START ->Unit
                   MainAction.PAUSE ->resume=stateFlow.value.seconds
                   MainAction.RESET->resume=0
                }
            }
            .flatMapLatest { action ->
                when (action) {
                    MainAction.START -> {
                        tickerFlow(resume)
                            .map {
                                MainState(
                                   seconds = it,
                                    watchState = MainState.WatchState.RUNNING
                                )
                            }
                            .onCompletion {
                                emit(initial)
                            }
                    }
                    MainAction.PAUSE -> {
                        flowOf(
                            MainState(
                                MainState.WatchState.PAUSED, resume
                            )
                        )
                    }
                    MainAction.RESET -> flowOf(initial)
                }
            }
                    .onEach { stateFlow.value = it }
                    .onEach { Log.d("MainVM", "Main state: $it") }
                    .catch { Log.d("MainVM", "Exception: $it") }
                    .launchIn(viewModelScope)

    }
    private fun tickerFlow(resume: Long): Flow<Long> {
        return generateSequence(resume + 1) { it + 1 }
            .asFlow()
            .onEach { delay(1_000) }
            .onStart { emit(resume) }
            .takeWhile { it <= MAX_SECONDS }
    }

    private companion object {
        const val MAX_SECONDS = 10
    }
}
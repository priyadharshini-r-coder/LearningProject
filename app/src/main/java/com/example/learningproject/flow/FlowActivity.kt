package com.example.learningproject.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.CheckResult
import androidx.lifecycle.lifecycleScope
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityFlowBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class FlowActivity : AppCompatActivity() {
    private val vm by viewModels<MainVM>()
    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityFlowBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        vm.stateFlow.collectIn(this) { render(it) }

        actionFlow()
            .onEach { vm.process(it) }
            .launchIn(lifecycleScope)
    }
    private fun actionFlow(): Flow<MainAction> {
        return merge(
            binding.buttonStart.clicks().map { MainAction.START },
            binding.buttonPause.clicks().map { MainAction.PAUSE },
            binding.buttonReset.clicks().map { MainAction.RESET },
        )
    }

    private fun render(state: MainState) {
        val mm = (state.seconds / 60).toString().padStart(2, '0')
        val ss = (state.seconds % 60).toString().padStart(2, '0')
        binding.textView.text = "$mm:$ss"

        when (state.watchState) {
            MainState.WatchState.RUNNING -> {
                binding.buttonStart.run {
                    isEnabled = false
                    text = "START"
                }
                binding.buttonPause.isEnabled = true
                binding.buttonReset.isEnabled = true
            }
            MainState.WatchState.PAUSED -> {
                binding.buttonStart.run {
                    isEnabled = true
                    text = "RESUME"
                }
                binding.buttonPause.isEnabled = false
                binding.buttonReset.isEnabled = true
            }
            MainState.WatchState.IDLE -> {
                binding.buttonStart.run {
                    isEnabled = true
                    text = "START"
                }
                binding.buttonPause.isEnabled = false
                binding.buttonReset.isEnabled = false
            }
        }
    }
}


fun View.clicks(): Flow<Unit> {
    return callbackFlow {
        setOnClickListener { trySend(Unit) }
        awaitClose { setOnClickListener(null) }
    }
}
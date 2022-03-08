package com.example.learningproject.uberclone.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.learningproject.R
import com.example.learningproject.uberclone.coroutines.api.ApiHelperImpl
import com.example.learningproject.uberclone.coroutines.api.RetrofitBuilder
import com.example.learningproject.uberclone.coroutines.local.DatabaseBuilder
import com.example.learningproject.uberclone.coroutines.local.DatabaseHelperImpl
import com.example.learningproject.uberclone.coroutines.utils.Status
import com.example.learningproject.uberclone.coroutines.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.nav_header_home.*

class TwoLongRunningTask : AppCompatActivity() {
    private lateinit var viewModel: TwoLongRunningTasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_running_task)
        setupViewModel()
        setupLongRunningTask()
    }

    private fun setupLongRunningTask() {
        viewModel.getStatus().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    textView.text = it.data
                    textView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.startLongRunningTask()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        ).get(TwoLongRunningTasksViewModel::class.java)
    }
}
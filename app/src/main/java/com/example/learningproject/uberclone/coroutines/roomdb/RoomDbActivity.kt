package com.example.learningproject.uberclone.coroutines.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learningproject.R
import com.example.learningproject.uberclone.coroutines.api.ApiHelperImpl
import com.example.learningproject.uberclone.coroutines.api.RetrofitBuilder
import com.example.learningproject.uberclone.coroutines.local.DatabaseBuilder
import com.example.learningproject.uberclone.coroutines.local.DatabaseHelperImpl
import com.example.learningproject.uberclone.coroutines.parallelnetworkcall.User
import com.example.learningproject.uberclone.coroutines.utils.Status
import com.example.learningproject.uberclone.coroutines.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_parallel_network_calls.*
import kotlinx.android.synthetic.main.activity_splash.*

class RoomDbActivity : AppCompatActivity() {
    private lateinit var viewModel: RoomDBViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallel_network_calls)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter =
            UserAdapter(
                arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getUsers().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)))).get(RoomDBViewModel::class.java)
    }
}
package com.example.learningproject.rxjava.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivitySelectionBinding
import com.example.learningproject.rxjava.search.SearchActivity

class SelectionActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.operators.setOnClickListener {
            startActivity(Intent(this@SelectionActivity, OperatorsActivity::class.java))
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this@SelectionActivity, SearchActivity::class.java))
        }
    }
}




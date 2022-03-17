package com.example.learningproject.rxjava.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningproject.MapsActivity
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityOperatorsBinding
import com.example.learningproject.rxjava.operators.MapExampleActivity
import com.example.learningproject.rxjava.operators.SimpleActivity
import com.example.learningproject.rxjava.operators.ZipActivity

class OperatorsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOperatorsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding= ActivityOperatorsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.simple.setOnClickListener {
            startActivity(Intent(this,SimpleActivity::class.java))
        }
        binding.map.setOnClickListener {
            startActivity(Intent(this,MapExampleActivity::class.java))
        }
        binding.zip.setOnClickListener {
            startActivity(Intent(this,ZipActivity::class.java))
        }
    }
}
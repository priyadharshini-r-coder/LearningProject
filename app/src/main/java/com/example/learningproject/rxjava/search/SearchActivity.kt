package com.example.learningproject.rxjava.search

import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivitySearchBinding
import com.example.learningproject.databinding.ActivityTaskBinding
import com.example.learningproject.rxjava.utils.getQueryTextChangeObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {
    private lateinit var  binding:ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpSearchObservable()
    }

    private fun setUpSearchObservable() {
        binding.searchView.getQueryTextChangeObservable()
            .debounce(300,TimeUnit.MILLISECONDS)
            .filter {
                text->
                if(text.isEmpty())
                {
                    binding.textViewResult.text=""
                    return@filter false
                }
                else
                {
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .switchMap {
                query->
                dataFromNetwork(query)
                    .doOnError{
                        //handle error
                    }
                    .onErrorReturn{ "" }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result->
                binding.textViewResult.text=result
            }
    }
    /*
    * Simulation of network data
    * */
    private fun dataFromNetwork(query:String):io.reactivex.rxjava3.core.Observable<String>
    {
        return io.reactivex.rxjava3.core.Observable.just(true)
            .delay(2,TimeUnit.SECONDS)
            .map {
                query
            }
    }
}
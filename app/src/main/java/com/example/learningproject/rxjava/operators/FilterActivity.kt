package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityFilterBinding
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

class FilterActivity : AppCompatActivity() {
    companion object{
        private const val TAG="FilterActivity"
    }
    private lateinit var binding:ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFilterBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btn.setOnClickListener {
            doSomeWork()
        }

    }

    /*
    * simple example by using filter operator to emit only even value
    *
    */
    private fun doSomeWork() {
       Observable.just(1,2,3,4,5,6)
           .filter {
               value->
               return@filter value %2 ==0
           }
           .subscribe(getObserver())
    }

    private fun getObserver():io.reactivex.rxjava3.core.Observer<Int> {
        return object : io.reactivex.rxjava3.core.Observer<Int> {

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(value: Int) {
                binding.textView.append(" onNext : ")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                binding.textView.append(" value : $value")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onNext ")
                Log.d(TAG, " value : $value")
            }

            override fun onError(e: Throwable) {
                binding.textView.append(" onError : " + e.message)
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onError : " + e.message)
            }

            override fun onComplete() {
                binding.textView.append(" onComplete")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onComplete")
            }
        }
    }
    }

package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityMergeBinding
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MergeActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MergeActivity"
    }
    private lateinit var binding:ActivityMergeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding= ActivityMergeBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btn.setOnClickListener {
            doSomeWork()
        }
    }
    /*
    * Using merge operator to combine Observable : merge does not maintain
    * the order of Observable.
    * It will emit all the 7 values may not be in order
    * Ex - "A1", "B1", "A2", "A3", "A4", "B2", "B3" - may be anything
    */
    private fun doSomeWork() {
        val observableA = Observable.fromArray("A1", "A2", "A3", "A4")
        val observableB = Observable.fromArray("B1", "B2", "B3", "B4")

        Observable.merge(observableA, observableB)
            .subscribe(getObserver())
    }
    private fun getObserver(): Observer<String> {
        return object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(value: String) {
                binding.textView.append(" onNext : value : $value")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onNext : value : $value")
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
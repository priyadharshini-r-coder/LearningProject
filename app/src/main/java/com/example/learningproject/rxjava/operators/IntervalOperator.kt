package com.example.learningproject.rxjava.operators

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityIntervalOperatorBinding
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class IntervalOperator : AppCompatActivity() {
    companion object {
        private const val TAG = "IntervalOperatorActivity"
    }
  private lateinit var binding:ActivityIntervalOperatorBinding
  private final var disposables=CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityIntervalOperatorBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
       binding.btn.setOnClickListener {
           doSomeWork()
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun doSomeWork() {
      disposables.add(getObserverable()?.subscribeOn(Schedulers.io())
          ?.observeOn(AndroidSchedulers.mainThread())?.subscribeWith(getObserver()))
    }

    private fun getObserverable():Observable<Long>? {
     return Observable.interval(0,2,TimeUnit.SECONDS)
    }


    private fun getObserver():DisposableObserver<Long>{
     return object: DisposableObserver<Long>(){
         @SuppressLint("LongLogTag")
         override fun onNext(t: Long?) {
             binding.textView.append("OnNext:value:" +t)
             binding.textView.append(AppConstant.LINE_SEPARATOR)
             Log.d(TAG,"onNext:value:" +t)
         }

         @SuppressLint("LongLogTag")
         override fun onError(e: Throwable?) {
             binding.textView.append("OnNext:value:" +e?.message)
             binding.textView.append(AppConstant.LINE_SEPARATOR)
             Log.d(TAG,"onNext:value:" +e?.message)
         }

         @SuppressLint("LongLogTag")
         override fun onComplete() {
             binding.textView.append("OnComplete")
             binding.textView.append(AppConstant.LINE_SEPARATOR)
             Log.d(TAG,"onNext:value:" +"OnComplete")
         }

     }

    }
}
package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.learningproject.databinding.ActivityAsyncBinding
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

class DistinctActivity : AppCompatActivity() {
    companion object{
        private val TAG="DistinctActivity"
    }
    private lateinit var binding: ActivityAsyncBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityAsyncBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btn.setOnClickListener {
            doSomeWork()
        }
    }

    private fun doSomeWork() {
        getObservable().distinct().subscribe(getObserver())
    }

    private fun getObservable():Observable<Int>{
     return  Observable.just(1,2,1,1,2,3,4,6,4)
    }

    private fun getObserver():Observer<Int>
    {
        return object : Observer<Int>
        {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, " onSubscribe : " + d?.isDisposed());
            }

            override fun onNext(value: Int?) {
                binding.textView.append(" onNext : value : " + value);
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onNext value : " + value);
            }

            override fun onError(e: Throwable?) {
                binding.textView.append(" onError : " + e?.message);
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e?.message);
            }

            override fun onComplete() {
                binding.textView.append(" onComplete");
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }

        }
    }
    }


}
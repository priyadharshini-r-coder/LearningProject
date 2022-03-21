package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityAsyncBinding
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

class ReplaySubjectActivity : AppCompatActivity() {
    companion object
    {
        private   const val  TAG="ReplayActivity"
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
    /* ReplaySubject emits to any observer all of the items that were emitted
      * by the source Observable, regardless of when the observer subscribes.
      */
    private fun doSomeWork() {
        val source = ReplaySubject.create<Int>()

        source.subscribe(getFirstObserver())
        // it will emit only 4 and onComplete


        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.onNext(4)
        source.onComplete()


         source.subscribe(getSecondObserver())


    }

    private fun getFirstObserver(): Observer<Int> {
        return object : Observer<Int>
        {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, " First onSubscribe : " + d?.isDisposed());
            }

            override fun onNext(t: Int?) {
                binding.textView.append(" First onNext : value : " + t);
                binding. textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " First onNext value : " + t);
            }

            override fun onError(e: Throwable?) {
                binding.textView.append("First onError:" + (e?.message ))
                binding. textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " First onError : " + e?.message);
            }

            override fun onComplete() {
                binding.textView.append(" First onComplete");
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " First onComplete");
            }

        }

    }

    private fun getSecondObserver(): Observer<Int> {
        return object: Observer<Int>
        {
            override fun onSubscribe(d: Disposable?) {
                binding.textView.append(" Second onSubscribe : isDisposed :" + d?.isDisposed());
                Log.d(TAG, " Second onSubscribe : " + d?.isDisposed());
                binding.textView.append(AppConstant.LINE_SEPARATOR);
            }

            override fun onNext(t: Int?) {
                binding.textView.append(" Second onNext : value : " + t);
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " Second onNext value : " + t);
            }

            override fun onError(e: Throwable?) {
                binding.textView.append(" Second onError : " + e?.message);
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " Second onError : " + e?.message);
            }

            override fun onComplete() {
                binding.textView.append(" Second onComplete");
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " Second onComplete");
            }

        }

    }
}
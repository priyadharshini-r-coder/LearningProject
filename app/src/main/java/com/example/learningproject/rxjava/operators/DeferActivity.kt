package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityAsyncBinding
import com.example.learningproject.rxjava.model.Car
import com.example.learningproject.rxjava.utils.AppConstant
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class DeferActivity : AppCompatActivity() {

    companion object{
        private val TAG="DeferActivity"
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
        val car = Car()

        val brandDeferObservable: Observable<String?>? = car.brandDeferObservable()

        car.setBrand("BMW")
        // Even if we are setting the brand after creating Observable

        // we will get the brand as BMW.
        // If we had not used defer, we would have got null as the brand.


        brandDeferObservable
            ?.subscribe(getObserver())
    }

    private fun getObserver(): Observer<String?> {
     return object:Observer<String?>
     {
         override fun onSubscribe(d: Disposable?) {
             Log.d(TAG, " onSubscribe : " + d?.isDisposed());
         }

         override fun onNext(value: String?) {
             binding.textView.append(" onNext : value : " + value);
             binding.textView.append(AppConstant.LINE_SEPARATOR);
             Log.d(TAG, " onNext : value : " + value);
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
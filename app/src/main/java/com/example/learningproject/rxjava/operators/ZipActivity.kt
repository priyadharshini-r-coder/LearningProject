package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityZipBinding
import com.example.learningproject.rxjava.model.User
import com.example.learningproject.rxjava.utils.AppConstant
import com.example.learningproject.rxjava.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

class ZipActivity : AppCompatActivity() {
    companion object{
        private const val TAG="ZipActivity"
    }
    private lateinit var binding:ActivityZipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding= ActivityZipBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btn.setOnClickListener {
            doSomeWork()
        }
    }
    /*
        * Here we are getting two user list
        * One, the list of cricket fans
        * Another one, the list of football fans
        * Then we are finding the list of users who loves both
        */
    private fun doSomeWork() {
        Observable.zip(getCricketFansObservable(),getFootballFansObservable(),
        BiFunction<List<User>,List<User>,List<User>>{
            cricketFans,footballFans ->
            return@BiFunction Utils.filterUserWhoLovesBoth(cricketFans,footballFans)
        })
        //run on a background thread
            .subscribeOn(Schedulers.io())
        //be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }



    private fun getFootballFansObservable(): ObservableSource<List<User>>? {
    return Observable.create(ObservableOnSubscribe<List<User>> {
        e->
        if(!e.isDisposed)
        {
            e.onNext(Utils.getUserListWhoLovesFootball())
            e.onComplete()
        }

    }).subscribeOn(Schedulers.io())
    }

    private fun getCricketFansObservable(): ObservableSource<List<User>>? {
        return Observable.create(ObservableOnSubscribe<List<User>> { e ->
            if (!e.isDisposed) {
                e.onNext(Utils.getUserListWhoLovesCricket())
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }
    private fun getObserver(): Observer<List<User>>{
        return object :Observer<List<User>>
        {
            override fun onSubscribe(d: Disposable?) {
                if (d != null) {
                    Log.d(TAG, " onSubscribe : " + d.isDisposed)
                }
            }

            override fun onNext(userList: List<User>?) {
               binding.textView.append("onNext")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                if (userList != null) {
                    for(user in userList ) {
                        binding.textView.append("firstname: ${user.firstname}")
                        binding.textView.append(AppConstant.LINE_SEPARATOR)
                    }
                }
                Log.d(TAG, " onNext : " + userList?.size)
            }

            override fun onError(e: Throwable?) {
                binding.textView.append(" onError : " + e?.message)
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onError : " + e?.message)
            }

            override fun onComplete() {
                binding.textView.append(" onComplete")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onComplete")
            }

        }


    }
}
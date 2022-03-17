package com.example.learningproject.rxjava.operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityMapExampleBinding
import com.example.learningproject.databinding.ActivityMapsBinding
import com.example.learningproject.rxjava.model.ApiUser
import com.example.learningproject.rxjava.model.User
import com.example.learningproject.rxjava.utils.AppConstant
import com.example.learningproject.rxjava.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MapExampleActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MapExampleActivity"
    }
    private lateinit var binding:ActivityMapExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMapExampleBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.btn.setOnClickListener {
            doSomeWork()
        }
    }
    /*
  * Here we are getting ApiUser Object from api server
  * then we are converting it into User Object because
  * may be our database support User Not ApiUser Object
  * Here we are using Map Operator to do that
  */
    private fun doSomeWork() {
        getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{
                apiUsers->
                return@map Utils.convertApiUserListToUserList(apiUsers)
            }
            .subscribe(getObserver())
    }
    private fun getObservable(): Observable<List<ApiUser>> {
        return Observable.create { e ->
            if (!e.isDisposed) {
                e.onNext(Utils.getApiuserList())
                e.onComplete()
            }
        }
    }

    private fun getObserver(): Observer<List<User>> {
        return object : Observer<List<User>> {

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
                binding.textView.append(" onNext")
                binding.textView.append(AppConstant.LINE_SEPARATOR)
                for (user in userList) {
                    binding.textView.append(" firstname : ${user.firstname}")
                    binding.textView.append(AppConstant.LINE_SEPARATOR)
                }
                Log.d(TAG, " onNext : " + userList.size)
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
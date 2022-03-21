package com.example.learningproject.rxjava.caching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityAsyncBinding;
import com.example.learningproject.databinding.ActivityCacheExampleBinding;
import com.example.learningproject.rxjava.caching.model.Data;
import com.example.learningproject.rxjava.utils.AppConstant;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CacheExampleActivity extends AppCompatActivity {
    private static final String TAG = CacheExampleActivity.class.getSimpleName();
    private ActivityCacheExampleBinding binding;

    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_cache_example);


        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomeWork();
            }
        });

        dataSource = new DataSource(new MemoryDataSource(), new DiskDataSource(), new NetworkDataSource());
    }

    private void doSomeWork() {

        Observable<Data> memory = dataSource.getDataFromMemory();
        Observable<Data> disk = dataSource.getDataFromDisk();
        Observable<Data> network = dataSource.getDataFromNetwork();

        Observable.concat(memory, disk, network)
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(getObserver());
    }

    private Observer<Data> getObserver() {
        return new Observer<Data>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Data data) {
                binding.textView.append(" onNext : " + data.source);
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onNext : " + data.source);
            }

            @Override
            public void onError(Throwable e) {
                binding.textView.append(" onError : " + e.getMessage());
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                binding.textView.append(" onComplete");
                binding.textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }

}
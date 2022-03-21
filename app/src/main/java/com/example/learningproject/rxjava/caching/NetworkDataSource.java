package com.example.learningproject.rxjava.caching;

import com.example.learningproject.rxjava.caching.model.Data;

import io.reactivex.rxjava3.core.Observable;

public class NetworkDataSource {
    public Observable<Data> getData() {
        return Observable.create(emitter -> {
            Data data = new Data();
            data.source = "network";
            emitter.onNext(data);
            emitter.onComplete();
        });
    }
}

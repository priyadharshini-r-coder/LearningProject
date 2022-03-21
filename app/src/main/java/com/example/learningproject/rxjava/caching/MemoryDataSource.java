package com.example.learningproject.rxjava.caching;

import com.example.learningproject.rxjava.caching.model.Data;

import io.reactivex.rxjava3.core.Observable;

public class MemoryDataSource {
    private Data data;

    public Observable<Data> getData() {
        return Observable.create(emitter -> {
            if (data != null) {
                emitter.onNext(data);
            }
            emitter.onComplete();
        });
    }

    public void cacheInMemory(Data data) {
        this.data = data.clone();
        this.data.source = "memory";
    }
}

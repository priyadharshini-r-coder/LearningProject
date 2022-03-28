package com.example.learningproject.rxjava.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Supplier
import java.util.concurrent.Callable


class Car {
    private var brand: String? = null

    fun setBrand(brand: String?) {
        this.brand = brand
    }

    fun brandDeferObservable(): Observable<String?>? {
        return Observable.defer(object : Callable<ObservableSource<out String?>?>,
            @io.reactivex.rxjava3.annotations.NonNull Supplier<ObservableSource<out String>> {
            override fun call(): ObservableSource<out String?>? {
                return Observable.just(brand)
            }

            override fun get(): ObservableSource<out String?>? {
                return Observable.just(brand)
            }
        })
    }
}
package com.app.ichsanulalifwan.barani.core.utils

import io.reactivex.Completable
import reactor.adapter.rxjava.RxJava2Adapter
import reactor.core.publisher.Mono

fun Completable.toMono(): Mono<Void> {
    return RxJava2Adapter.completableToMono(this)
}
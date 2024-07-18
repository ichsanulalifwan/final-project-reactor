package com.app.ichsanulalifwan.barani.core.data.location

import android.location.Address
import android.location.Location
import com.app.ichsanulalifwan.barani.core.data.repository.location.AddressRepository
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

internal fun getAddresses(
    addressRepository: AddressRepository,
    location: Location,
    maxResults: Int,
): Mono<List<Address>> = Mono.create { sink: MonoSink<List<Address>> ->

    addressRepository.getAddresses(
        location.longitude,
        location.latitude,
        maxResults,
    ) { addresses, throwable ->
        if (addresses != null) {
            sink.success(addresses)
        } else {
            val exception = checkNotNull(throwable) {
                "If the result is null, the exception cannot be."
            }
            sink.error(exception)
        }
    }
}
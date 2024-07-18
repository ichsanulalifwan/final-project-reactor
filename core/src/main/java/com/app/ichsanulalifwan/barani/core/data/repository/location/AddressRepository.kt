package com.app.ichsanulalifwan.barani.core.data.repository.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.app.ichsanulalifwan.barani.core.utils.Constant.US_COUNTRY_CODE
import java.util.Locale

class AddressRepository(context: Context, val isMock: Boolean) {

    private val geoCoder = Geocoder(context)

    fun getAddresses(
        longitude: Double,
        latitude: Double,
        maxResults: Int,
        block: (List<Address>?, Throwable?) -> Unit,
    ) {
        try {
            val mockAddress = Address(Locale.US).apply { countryCode = US_COUNTRY_CODE }
            block(
                if (isMock) listOf(mockAddress) else geoCoder.getFromLocation(
                    latitude,
                    longitude,
                    maxResults,
                ), null
            )
        } catch (t: Throwable) {
            block(null, t)
        }
    }
}
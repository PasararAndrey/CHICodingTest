package dev.chicodingtest

import android.os.Parcel
import android.os.Parcelable

class Counter(initialValue: Int = 0) : Parcelable {
    var countValue = initialValue

    constructor(parcel: Parcel) : this() {
        countValue = parcel.readInt()
    }

    fun increment() {
        countValue++
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(countValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Counter> {
        override fun createFromParcel(parcel: Parcel): Counter {
            return Counter(parcel)
        }

        override fun newArray(size: Int): Array<Counter?> {
            return arrayOfNulls(size)
        }

        //The key to be stored in bundle
        const val COUNTER_BUNDLE_KEY = "counter_state"

        //The key for activity/fragment requests
        const val COUNTER_REQUEST_KEY = "counter_request_key"

    }
}
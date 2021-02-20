package com.ainsigne.interactivelivestreaming.utils

import android.os.Build


/**
 * Constants used throughout the app.
 */

val FAKE_BUILD = "fake"



fun isEmulator(): Boolean {
    return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator"))
}



const val DATABASE_NAME = "stream-db"
const val STREAMS_FILE = "streams.json"
const val LOCAL_BASE_API = "http://192.168.254.104:8080/api/v1/"
const val BASE_API = "https://stream-api-ainsigne.herokuapp.com/api/v1/"
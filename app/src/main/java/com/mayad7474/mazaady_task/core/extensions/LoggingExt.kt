package com.mayad7474.mazaady_task.core.extensions

import android.util.Log
import com.mayad7474.mazaady_task.BuildConfig

fun Any.logd(tag: String = "AppDebug") {
    if (BuildConfig.DEBUG)
        Log.d(tag, "$tag: $this")
}

fun Any.loge(tag: String = "AppDebug") {
    if (BuildConfig.DEBUG)
        Log.e(tag, "$tag: $this")
}

fun Any.logw(tag: String = "AppDebug") {
    if (BuildConfig.DEBUG)
        Log.w(tag, "$tag: $this")
}
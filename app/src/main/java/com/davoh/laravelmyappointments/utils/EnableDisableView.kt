package com.davoh.laravelmyappointments.utils

import android.view.View

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

inline fun <T : View> T.enableIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        enable()
    } else {
        disable()
    }
}

inline fun <T : View> T.disableIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        disable()
    } else {
        enable()
    }
}
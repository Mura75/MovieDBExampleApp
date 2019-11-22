package com.mobile.coroutineapplication.presentation

import android.app.Activity
import android.widget.Toast


fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Int.toDpi() {
    this * 16
}
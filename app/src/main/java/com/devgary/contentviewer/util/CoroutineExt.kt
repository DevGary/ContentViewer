package com.devgary.contentviewer.util

import kotlinx.coroutines.Job

fun Job?.cancel() {
    this?.cancel()
}
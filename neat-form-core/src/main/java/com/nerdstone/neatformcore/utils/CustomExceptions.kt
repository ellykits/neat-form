package com.nerdstone.neatformcore.utils

import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

object CustomExceptions {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("CoroutineExceptionHandler got $exception")
    }
}
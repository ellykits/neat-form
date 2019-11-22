package com.nerdstone.neatformcore

import com.nerdstone.neatformcore.utils.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

class TestCoroutineContextProvider : CoroutineContextProvider {

    @ExperimentalCoroutinesApi
    override val main: CoroutineContext
        get() = Dispatchers.Main

    @ExperimentalCoroutinesApi
    override val io: CoroutineContext
        get() = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    override val default: CoroutineContext
        get() = Dispatchers.Unconfined

}
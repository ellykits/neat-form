package com.nerdstone.neatformcore

import com.nerdstone.neatformcore.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestRule, TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    val testDispatcherProvider = object : DispatcherProvider {
        override fun default() = testDispatcher
        override fun io() = testDispatcher
        override fun main() = testDispatcher
        override fun unconfined() = testDispatcher
    }

    override fun apply(base: Statement?, description: Description?) =
        object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                base?.evaluate()
                Dispatchers.resetMain()
                testDispatcher.cleanupTestCoroutines()
            }
        }
}
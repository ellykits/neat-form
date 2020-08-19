package com.nerdstone.neatformcore.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.ref.WeakReference
import java.util.*

/**
 * A helper class to execute tasks sequentially in coroutines.
 *
 * Calling [afterPrevious] will always ensure that all previously requested work completes prior to
 * calling the block passed. Any future calls to [afterPrevious] while the current block is running
 * will wait for the current block to complete before starting.
 *
 * Code obtained from this gist https://gist.github.com/objcode/7ab4e7b1df8acd88696cb0ccecad16f7#file-concurrencyhelpers-kt-L49
 */
class SingleRunner {
    /**
     * A coroutine mutex implements a lock that may only be taken by one coroutine at a time.
     */
    private val mutex = Mutex()

    /**
     * Ensure that the block will only be executed after all previous work has completed.
     *
     * When several coroutines call afterPrevious at the same time, they will queue up in the order
     * that they call afterPrevious. Then, one coroutine will enter the block at a time.
     *
     * In the following example, only one save operation (user or song) will be executing at a time.
     *
     * ```
     * class UserAndSongSaver {
     *    val singleRunner = SingleRunner()
     *
     *    fun saveUser(user: User) {
     *        singleRunner.afterPrevious { api.post(user) }
     *    }
     *
     *    fun saveSong(song: Song) {
     *        singleRunner.afterPrevious { api.post(song) }
     *    }
     * }
     * ```
     *
     * @param block the code to run after previous work is complete.
     */
    suspend fun <T> afterPrevious(block: suspend () -> T): T {
        // Before running the block, ensure that no other blocks are running by taking a lock on the
        // mutex.

        // The mutex will be released automatically when we return.

        // If any other block were already running when we get here, it will wait for it to complete
        // before entering the `withLock` block.
        mutex.withLock {
            return block()
        }
    }
}

interface DispatcherProvider {
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class DefaultDispatcherProvider : DispatcherProvider

/**
 * @author Elly Nerdstone
 *
 * This is a weak collection list that internally uses [LinkedList]. We use this to hold weak references
 * of items in order to avoid memory leaks as in the case of caching listeners in a singleton class
 */
class DisposableList<T> {

    private val linkedList: LinkedList<WeakReference<T>> = LinkedList()

    /**
     * Add a new [item] to the linked list. We first check if the item already exists in the list
     * if not we add it otherwise we return false.
     */
    fun add(item: T): Boolean {
        val currentList = get()
        for (oldItem in currentList) {
            if (item === oldItem) {
                return false
            }
        }
        return linkedList.add(WeakReference<T>(item))
    }

    /**
     * This method is used to retrieve a list of elements of type [T]. It also check if list has null
     * values on the [WeakReference]. If so the items will be removed from the list. This is useful when
     * trying to avoid memory leaks by the list maintaining references to items that had been garbage
     * collected
     */
    fun get(): MutableList<T> {
        val finalList = arrayListOf<T>()
        val itemsToRemove = LinkedList<WeakReference<T>>()
        linkedList.forEach { weakReference ->
            when (val item: T? = weakReference.get()) {
                null -> itemsToRemove.add(weakReference)
                else -> finalList.add(item)
            }
        }
        itemsToRemove.forEach { weakReference ->
            linkedList.remove(weakReference)
        }
        return finalList
    }

    /**
     * This method is used to remove the provided [item] from the [linkedList]
     */
    fun remove(item: T): Boolean {
        var weakReferenceToRemove: WeakReference<T>? = null
        for (weakReference in linkedList) {
            val currentItem: T? = weakReference.get()
            if (currentItem === item) {
                weakReferenceToRemove = weakReference
                break
            }
        }
        return if (weakReferenceToRemove != null) {
            linkedList.remove(weakReferenceToRemove)
            true
        } else false
    }
}
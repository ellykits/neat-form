package com.nerdstone.neatformcore.junit.utils

import com.nerdstone.neatformcore.utils.DisposableList
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DisposableListTest {

    private val disposableList = DisposableList<String>()

    @Before
    fun `Before everything`() {
        disposableList.add("item1")
    }

    @Test
    fun `Should add a new item to the list`() {
        Assert.assertTrue(disposableList.add("item2"))
        Assert.assertFalse(disposableList.add("item1"))
        Assert.assertEquals(disposableList.get().size, 2)
    }

    @Test
    fun `Should return list items`() {
        Assert.assertEquals(disposableList.get().size, 1)
    }

    @Test
    fun `Should remove an item from the list`() {
        disposableList.add("item2")
        Assert.assertTrue(disposableList.remove("item2"))
        Assert.assertFalse(disposableList.remove("item3"))
        Assert.assertEquals(disposableList.get().size, 1)
    }
}
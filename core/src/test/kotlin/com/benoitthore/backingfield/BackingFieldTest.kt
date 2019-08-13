package com.benoitthore.backingfield

import org.junit.Assert.*
import org.junit.Test


private val <T> T.print get() = apply { println(this) }

class BackingFieldTest {

    private val defaultValue = "defaultValue"

    private val holder =
        BackingField<Any, String> { "defaultValue" }
    private var Any.extraValue: String by holder

    data class SomeClass(val i: Int = 0)

    @Test
    fun test_value_assign() {
        val object1 = SomeClass()
        val object2 = SomeClass()
        val object3 = SomeClass()

        val object1Value = "object1Value"
        val object2Value = "object2Value"

        object1.extraValue = object1Value
        object2.extraValue = object2Value


        assertEquals(object1Value, object1.extraValue)
        assertEquals(object2Value, object2.extraValue)
        assertEquals(defaultValue, object3.extraValue)

    }


    @Test
    fun test_re_assign_keeps_map_size_consistent() {
        val object1 = SomeClass()

        val value1 = "11111"
        val value2 = "22222"

        object1.extraValue = value1
        object1.extraValue = value2

        assertEquals(value2, object1.extraValue)

        assertEquals(1, holder.referenceMap.size)


    }


    /////////////////////
    /////////////////////
    //// DEBUG TESTS ////
    /////////////////////
    /////////////////////

    val onGcCallback = { before: Int, after: Int ->
        println("GC : $before -> $after")
    }

    val byteArrayValueHolder = BackingField<Any, ByteArray>(onGcCallback) { byteArrayOf(0) }
    var Any.someByteArray: ByteArray by byteArrayValueHolder

    @Test
    fun garbage_collection_test() {

        val bigNumber = 1_000_000
        // Allocates a lot and see if it crashes


        (0..1_000).forEach {
            "0 ${Math.random()}".someByteArray = ByteArray(bigNumber)
        }

        "Finished".print


        byteArrayValueHolder.referenceMap.size.print
    }

    @Test
    fun identity_weak_ref_garbage_collection_test() {

        val bigNumber = 10_000_000
        // Allocates a lot and see if it crashes


        val totalObjects = 1000
        val remaingObjects = (0..totalObjects).map {
            IdentityWeakReference(ByteArray(bigNumber))
        }.filter { it.get() != null }.size

        assertTrue(remaingObjects < totalObjects)
    }


}


package com.benoitthore.backingfield

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference


class IdentityWeakReference<T> : WeakReference<T> {

    private val hash: Int


    constructor(value: T) : super(value) {
        hash = value.generateHash()
    }

    constructor(value: T, referenceQueue: ReferenceQueue<in T>?) : super(value, referenceQueue) {
        hash = value.generateHash()
    }

    fun <T> refEquals(other: T?): Boolean = other == get()

    override fun equals(other: Any?): Boolean {
        return (other as? WeakReference<T>)?.let { it.get() === get() } ?: false
    }

    override fun hashCode(): Int {
        return hash
    }


    private inline fun <T> T.generateHash(): Int = System.identityHashCode(this)
}


package com.benoitthore.backingfield


import java.lang.ref.ReferenceQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BackingField<K : Any, V> internal constructor(
    onGcCallback: ((Int, Int) -> Unit)?,
    val default: K.() -> V
) :
    ReadWriteProperty<K, V> {

    constructor(default: K.() -> V) : this(onGcCallback = null, default = default)

    private val referenceQueue = ReferenceQueue<K>()

    internal val referenceMap: ConcurrentHashMap<IdentityWeakReference<K>, V> =
        referenceMap(referenceQueue, onGcCallback)

    override fun getValue(thisRef: K, property: KProperty<*>): V =
        referenceMap[thisRef.toIdentity()]
            ?: thisRef.default().apply { setValue(thisRef, property, this) }

    override fun setValue(thisRef: K, property: KProperty<*>, value: V) {
        referenceMap[thisRef.toIdentity()] = value
    }

    // TODO Refactor so this isn't needed (allocating on every get and set is bad)
    fun K.toIdentity() = IdentityWeakReference(this, referenceQueue)

}


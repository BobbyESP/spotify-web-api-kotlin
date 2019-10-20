package com.adamratzman.spotify.utils

expect class ConcurrentHashMap<K, V>() {
    operator fun get(key: K): V?
    fun put(key: K, value: V): V?
    fun remove(key: K): V?
    fun clear()

    val size: Int
    val entries: MutableSet<MutableMap.MutableEntry<K, V>>
}

expect fun <K, V> ConcurrentHashMap<K, V>.toList(): List<Pair<K, V>>

expect class BufferedImage

expect class File
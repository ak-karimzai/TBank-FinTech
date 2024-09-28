package com.akkarimzai.task5.persistence.utils

import java.util.concurrent.ConcurrentHashMap

class InMemoryStore<K, V> {
    var collection: ConcurrentHashMap<K, V> = ConcurrentHashMap<K, V>()
}
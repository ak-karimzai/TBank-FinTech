package com.akkarimzai.utils;

import java.util.function.Consumer;

public interface CustomIterator<E> {
    void forEachRemaining(Consumer<? super E> action);
    boolean hasNext();
    E next();
}

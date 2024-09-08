package com.akkarimzai.utils;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomLinkedListCollector<T> implements Collector<T, CustomLinkedList<T>, CustomLinkedList<T>> {
    @Override
    public Supplier<CustomLinkedList<T>> supplier() {
        return CustomLinkedList::new;
    }

    @Override
    public BiConsumer<CustomLinkedList<T>, T> accumulator() {
        return CustomLinkedList::add;
    }

    @Override
    public BinaryOperator<CustomLinkedList<T>> combiner() {
        return (lst1, lst2) -> {
            lst2.forEach(lst1::add);
            return lst1;
        };
    }

    @Override
    public Function<CustomLinkedList<T>, CustomLinkedList<T>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }

    public static <T> CustomLinkedListCollector<T> toCustomLinkedList() {
        return new CustomLinkedListCollector<>();
    }
}

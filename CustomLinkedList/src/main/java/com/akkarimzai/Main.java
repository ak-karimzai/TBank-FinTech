package com.akkarimzai;

import com.akkarimzai.utils.CustomLinkedList;
import com.akkarimzai.utils.CustomLinkedListCollector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        CustomLinkedList<Integer> customList = new CustomLinkedList<>();

        customList.add(1);
        customList.add(2);
        customList.add(3);
        customList.forEach(System.out::println);

        int elementAtZero = customList.get(0);
        System.out.println("Element at index 0: " + elementAtZero);

        customList.remove();
        System.out.println("After removing first element: ");
        customList.iterator().forEachRemaining(System.out::println);

        boolean containsElement = customList.contains(2);
        System.out.println("List contains 2: " + containsElement);

        List<Integer> anotherList = Arrays.asList(4, 5, 6);
        customList.addAll(anotherList);
        System.out.println("After adding all elements from another collection: ");
        customList.iterator().forEachRemaining(System.out::println);

        Stream<Integer> stream = Stream.of(7, 8, 9);
        CustomLinkedList<Integer> collectedList = stream
                .collect(CustomLinkedListCollector.toCustomLinkedList());
        System.out.println("collected CustomLinkedList from stream: ");
        collectedList.iterator().forEachRemaining(System.out::println);


        stream = Stream.of(1, 2, 3);
        CustomLinkedList<Integer> reducedList = stream.reduce(
                new CustomLinkedList<>(),
                (list, elem) -> {
                    list.add(elem);
                    return list;
                },
                (lhs, rhs) -> {
                    rhs.iterator().forEachRemaining(lhs::add);
                    return lhs;
                }
        );

        System.out.println("CustomLinkedList created from stream: ");
        reducedList.iterator().forEachRemaining(System.out::println);
    }
}
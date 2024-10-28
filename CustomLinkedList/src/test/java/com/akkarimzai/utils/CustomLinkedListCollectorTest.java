package com.akkarimzai.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class CustomLinkedListCollectorTest {
    @Test
    void customCollectCollectsCorrectly() {
        // Arrange
        Stream<Integer> stream = IntStream.range(0, 10).boxed();
        int streamSize = 10;

        // Act
        var customList = stream
                .collect(CustomLinkedListCollector.toCustomLinkedList());

        // Assert
        Assertions.assertThat(customList.size())
                .isEqualTo(streamSize);
    }

    @Test
    void streamElementsAddedInSameOrder() {
        // Arrange
        List<Integer> list = IntStream.range(0, 10).boxed().toList();
        Stream<Integer> stream = list.stream();

        // Act
        var customList = stream
                .collect(CustomLinkedListCollector.toCustomLinkedList());

        // Assert
        for (int i = 0; i < customList.size(); i++) {
            Assertions.assertThat(customList.get(i))
                    .isEqualTo(list.get(i));
        }
    }

    @Test
    void collectElementsUsingReduce() {
        // Arrange
        Stream<Integer> stream = Stream.of(1, 2, 3);
        CustomLinkedList<Object> reducedList = stream.reduce(
                new CustomLinkedList<>(),
                (list, elem) -> {
                    list.add(elem);
                    return list;
                },
                (lhs, rhs) -> {
                    lhs.addAll(rhs);
                    return lhs;
                }
        );
        int reducedLength = 3;

        // Act
        int listSize = reducedList.size();

        // Arrange
        Assertions.assertThat(reducedList.size())
                .isEqualTo(listSize);
    }

    @Test
    void reducedListContainsAllElements() {
        // Arrange
        List<Integer> list = List.of(1, 2, 3);
        CustomLinkedList<Object> reducedList = list.stream().reduce(
                new CustomLinkedList<>(),
                (lst, elem) -> {
                    lst.add(elem);
                    return lst;
                },
                (lhs, rhs) -> {
                    lhs.addAll(rhs);
                    return lhs;
                }
        );
        int reducedLength = 3;

        // Act
        int listSize = reducedList.size();

        // Arrange
        Assertions.assertThat(reducedList.size())
                .isEqualTo(listSize);
        for (int i = 0; i < reducedLength; i++) {
            Assertions.assertThat(reducedList.get(i))
                    .isEqualTo(list.get(i));
        }
    }
}
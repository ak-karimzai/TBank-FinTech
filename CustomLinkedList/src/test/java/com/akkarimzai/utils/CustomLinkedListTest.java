package com.akkarimzai.utils;

import com.akkarimzai.exceptions.IndexOutOfBoundsException;
import com.akkarimzai.exceptions.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class CustomLinkedListTest {
    @Test
    void constructorCreatesEmptyLinkedList() {
        // Arrange
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        int emptyListSize = 0;

        // Act
        int size = list.size();

        // Assert
        Assertions.assertThat(size)
                .isEqualTo(emptyListSize);
    }

    @Test
    void constructorCreatesLinkedListFromCollectionInSameOrder() {
        // Arrange
        List<Integer> stdArrayList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        CustomLinkedList<Integer> list = new CustomLinkedList<>(
                stdArrayList
        );
        int size = stdArrayList.size();

        // Act
        int customListSize = list.size();

        // Assert
        Assertions.assertThat(size)
                .isEqualTo(customListSize);
        for (int i = 0; i < stdArrayList.size(); i++) {
            Assertions.assertThat(list.get(i))
                    .isEqualTo(stdArrayList.get(i));
        }
    }

    @Test
    void addAppendsElement() {
        // Assertion
        CustomLinkedList<Integer> customList = new CustomLinkedList<>();
        customList.add(10);
        int listSize = 1;

        // Act
        int customListSize = customList.size();

        // Assert
        Assertions.assertThat(customListSize)
                .isEqualTo(listSize);
    }

    @Test
    void addAppendsElementToTail() {
        // Assertion
        CustomLinkedList<Integer> customList = new CustomLinkedList<>();
        int[] array = IntStream.range(0, 100).toArray();
        int numberOfElements = 100;

        // Act
        Arrays.stream(array)
                .forEach(customList::add);
        int customListSize = customList.size();

        // Assert
        Assertions.assertThat(customListSize)
                .isEqualTo(numberOfElements);
        for (int i = 0; i < array.length; i++) {
            Assertions.assertThat(array[i])
                    .isEqualTo(customList.get(i));
        }
    }

    @Test
    void getThrowsIndexOutOfBoundException() {
        // Arrange
        CustomLinkedList<Integer> customList = new CustomLinkedList<>();

        // Act && Assert
        Assertions.assertThatCode(() -> customList.get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void containsReturnsTrueWhenElementExists() {
        // Arrange
        CustomLinkedList<Integer> list = new CustomLinkedList<>(
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        );

        // Act
        boolean containsElement = list.contains(1);

        // Assert
        Assertions.assertThat(containsElement)
                .isTrue();
    }

    @Test
    void containsReturnsFalseWhenElementDoesNotExist() {
        // Arrange
        CustomLinkedList<Integer> list = new CustomLinkedList<>(
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        );

        // Act
        boolean containsElement = list.contains(-2);

        // Assert
        Assertions.assertThat(containsElement)
                .isFalse();
    }

    @Test
    void addAllAppendsAllElements() {
        // Arrange
        CustomLinkedList<Integer> customList = new CustomLinkedList<>();
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Act
        customList.addAll(list);
        int customListSize = customList.size();

        // Assert
        Assertions.assertThat(customListSize)
                .isEqualTo(list.size());
        for (int i = 0; i < list.size(); i++) {
            Assertions.assertThat(list.get(i))
                    .isEqualTo(customList.get(i));
        }
    }

    @Test
    void addAllOnNonEmptyListExtendsList() {
        // Arrange
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        CustomLinkedList<Integer> customList = new CustomLinkedList<>(
                list
        );

        // Act
        customList.addAll(list);
        int customListSize = customList.size();

        // Assert
        Assertions.assertThat(customListSize)
                .isEqualTo(list.size() * 2);
        for (int i = 0; i < list.size(); i++) {
            Assertions.assertThat(list.get(i))
                    .isEqualTo(customList.get(list.size() + i));
        }
    }

    @Test
    void removeRemovesListHead() {
        // Arrange
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        CustomLinkedList<Integer> customList = new CustomLinkedList<>(list);

        // Act
        customList.remove();

        // Assert
        Assertions.assertThat(list.get(0))
                .isNotEqualTo(customList.get(0));
    }

    @Test
    void removeOnEmptyListThrowsNoSuchElementException() {
        // Arrange
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        // Act && Assert
        Assertions.assertThatThrownBy(list::remove)
                .isInstanceOf(NoSuchElementException.class);
    }
}
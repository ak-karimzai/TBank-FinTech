package com.akkarimzai.utils;

import com.akkarimzai.exceptions.IndexOutOfBoundsException;
import com.akkarimzai.exceptions.NoSuchElementException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CustomLinkedList() {
        this.size = 0;
    }

    public CustomLinkedList(Collection<? extends T> c) {
        this();
        this.addAll(c);
    }

    public CustomLinkedList(CustomLinkedList<T> c) {
        this();
        this.addAll(c);
    }

    public boolean add(T elem) {
        if (this.tail == null) {
            this.head = this.tail = new Node<>(null, elem, null);
        } else {
            Node<T> temp = new Node<>(this.tail, elem, null);
            this.tail.next = temp;
            this.tail = temp;
        }
        this.size++;
        return true;
    }

    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        boolean low = index < this.size >> 1;
        Node<T> temp = low ? this.head : this.tail;
        if (low) {
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            for (int i = this.size - 1; i > index; i--) {
                temp = temp.prev;
            }
        }

        return temp.item;
    }

    public void remove() {
        if (this.head == null) {
            throw new NoSuchElementException();
        }

        Node<T> curr = this.head;
        T elem = this.head.item;

        this.head = this.head.next;
        if (this.head != null) {
            this.head.prev = null;
        }

        this.size--;
        curr.item = null;
        curr.next = null;
        curr.prev = null;
    }

    public int size() {
        return this.size;
    }

    public boolean contains(Object item) {
        boolean found = false;
        for (
                Node<T> temp = this.head;
                temp != null && !found;
                temp = temp.next
        ) {
            found = temp.item.equals(item);
        }
        return found;
    }

    public boolean addAll(Collection<? extends T> c) {
        int sizeBeforeAppendAll = this.size;
        c.forEach(this::add);
        return sizeBeforeAppendAll != this.size;
    }

    public boolean addAll(CustomLinkedList<? extends T> c) {
        int sizeBeforeAppendAll = this.size;
        c.forEach(this::add);
        return sizeBeforeAppendAll != this.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomLinkedListIterator<>(head);
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private static class CustomLinkedListIterator<T> implements Iterator<T> {
        private Node<T> curr;

        public CustomLinkedListIterator(Node<T> head) {
            this.curr = head;
        }

        @Override
        public boolean hasNext() {
            return this.curr != null;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            T ret = curr.item;
            this.curr = curr.next;

            return ret;
        }
    }

    @SafeVarargs
    public static <E> CustomLinkedList<E> of(E... elements) {
        return new CustomLinkedList<>(List.of(elements));
    }
}

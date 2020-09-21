package com.fucct.tobispring.learning;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}

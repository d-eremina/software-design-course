package com.company;

import java.util.Collection;

public interface Martian<T> {
    T getGeneticCode();

    Martian<T> getParent();

    Collection<Martian<T>> getChildren();

    Collection<Martian<T>> getDescendants();

    Boolean hasChildWithValue(T value);

    Boolean hasDescendantWithValue(T value);
}

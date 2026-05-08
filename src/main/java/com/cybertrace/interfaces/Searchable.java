package com.cybertrace.interfaces;

import java.util.List;

public interface Searchable<T> {
    List<T> search(String query);
    List<T> filter(String field, String value);
}

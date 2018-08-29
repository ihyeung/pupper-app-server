package com.utahmsd.pupper.dao;

import jdk.nashorn.internal.runtime.options.Option;

import java.util.List;

public interface Repository<T, ID> {
    Option<T> find(ID id);

    Option<List<T>> findAll();

    Option<T> save(T t);

    Option<T> delete(T t);

}

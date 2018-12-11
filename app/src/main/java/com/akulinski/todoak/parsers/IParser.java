package com.akulinski.todoak.parsers;

/**
 * Parser interface
 * @param <T>
 */
public interface IParser<T> {

    void loadData(T data);

    void parse();

    Object getResult();
}

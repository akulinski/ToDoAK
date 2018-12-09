package com.akulinski.todoak.parsers;

public interface IParser<T> {

    void loadData(T data);
    void parse();
    Object getResult();
}

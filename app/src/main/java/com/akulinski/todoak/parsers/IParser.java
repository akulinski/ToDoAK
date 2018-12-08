package com.akulinski.todoak.parsers;

import com.google.gson.JsonArray;

public interface IParser {

    void loadData(JsonArray jsonArray);
    void parse();

}

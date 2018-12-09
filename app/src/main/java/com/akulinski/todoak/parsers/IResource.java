package com.akulinski.todoak.parsers;

import java.util.HashMap;

public interface IResource {

    HashMap<String,String> generateObjectInfo();

    IResource fromMap(HashMap<String,String> map);
}

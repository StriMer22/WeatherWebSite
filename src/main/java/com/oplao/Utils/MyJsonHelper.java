package com.oplao.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MyJsonHelper {

    public static Object getParam(HashMap map, String root, String param){

        return ((HashMap)((ArrayList)map.get(root)).get(0)).get(param);


    }


}

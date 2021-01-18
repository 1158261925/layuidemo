package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class DataFormat {

    public static String transJsonToString(int status, String msg, int count, List data){

        JSONObject json = new JSONObject();

        json.put("status",status);
        json.put("hint",msg);
        json.put("total",count);
        json.put("rows",data);

        return json.toString();

    }
}

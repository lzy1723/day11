package com.example.day11hx.utils;

public interface ResultCallBack {
    void onSuccess(String filePath, long duration);
    void onFail(String str);
}

package com.vise.bledemo.model;

public class send {
    int flag;//0永久 1 一次
    String data;

    public send(int flag, String data) {
        this.flag = flag;
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

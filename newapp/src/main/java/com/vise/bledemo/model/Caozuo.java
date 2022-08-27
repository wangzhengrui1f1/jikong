package com.vise.bledemo.model;

public class Caozuo {
//    public static final String caoZuo = "create table caoZuo(id integer primary key autoincrement,sbName char(100)," +
//            "numvalues char(200),sId char(20)" +
//            ",name char(30),time char(30),info char(100))" ;
    String sbName;
    String numvalues;
    String sId;
    String name;
    String time;
    String info;

    public Caozuo(String sbName, String numvalues, String sId, String name, String time, String info) {
        this.sbName = sbName;
        this.numvalues = numvalues;
        this.sId = sId;
        this.name = name;
        this.time = time;
        this.info = info;
    }

    public String getSbName() {
        return sbName;
    }

    public void setSbName(String sbName) {
        this.sbName = sbName;
    }

    public String getNumvalues() {
        return numvalues;
    }

    public void setNumvalues(String numvalues) {
        this.numvalues = numvalues;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

package com.vise.bledemo.model;

public class getCaozuo {
    int id;
    String sbName;
    String numvalues;
    String sId;
    String name;
    String time;
    String info;

    public getCaozuo(int id, String sbName, String numvalues, String sId, String name, String time, String info) {
        this.id = id;
        this.sbName = sbName;
        this.numvalues = numvalues;
        this.sId = sId;
        this.name = name;
        this.time = time;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

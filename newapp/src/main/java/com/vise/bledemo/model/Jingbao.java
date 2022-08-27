package com.vise.bledemo.model;

public class Jingbao {
    String title;
    String info;
    String name;
    String time;
    int grade;//1 2 3级别
    String locate;
    String jingdu;
    String weidu;
    String nongdu;
    String wendu;
    String guangqiang;
    String firsttime;
    String sid;

    public Jingbao(String title, String info, String name, String time, int grade, String locate, String jingdu, String weidu, String nongdu, String wendu, String guangqiang, String firsttime, String sid) {
        this.title = title;
        this.info = info;
        this.name = name;
        this.time = time;
        this.grade = grade;
        this.locate = locate;
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.nongdu = nongdu;
        this.wendu = wendu;
        this.guangqiang = guangqiang;
        this.firsttime = firsttime;
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public String getNongdu() {
        return nongdu;
    }

    public void setNongdu(String nongdu) {
        this.nongdu = nongdu;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGuangqiang() {
        return guangqiang;
    }

    public void setGuangqiang(String guangqiang) {
        this.guangqiang = guangqiang;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}

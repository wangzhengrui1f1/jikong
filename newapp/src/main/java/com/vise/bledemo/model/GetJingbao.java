package com.vise.bledemo.model;

public class GetJingbao {
    int id;
    String title;//标题
    String info;//备注
    String name;//人员
    String time;//
    int grade;//1 2 3级别
    String locate;
    String jingdu;
    String weidu;
    String nongdu;
    String wendu;
    String guangqiang;
    String firsttime;
    String sid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "GetJingbao{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", grade=" + grade +
                ", locate='" + locate + '\'' +
                ", jingdu='" + jingdu + '\'' +
                ", weidu='" + weidu + '\'' +
                ", nongdu='" + nongdu + '\'' +
                ", wendu='" + wendu + '\'' +
                ", guangqiang='" + guangqiang + '\'' +
                ", firsttime='" + firsttime + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}

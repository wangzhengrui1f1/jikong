package com.vise.bledemo.model;

public class IntentB_Q {
    String nd;
    int ndflag;
    String wd;
    int wdflag;
    String gq;
    int gqflag;
    //todo 超标等级
    int dengji;


    //todo 从蓝牙 - > 手动取证的浓度-温度-光强

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getGq() {
        return gq;
    }

    public void setGq(String gq) {
        this.gq = gq;
    }

    public int getNdflag() {
        return ndflag;
    }

    public void setNdflag(int ndflag) {
        this.ndflag = ndflag;
    }

    public int getWdflag() {
        return wdflag;
    }

    public void setWdflag(int wdflag) {
        this.wdflag = wdflag;
    }

    public int getGqflag() {
        return gqflag;
    }

    public void setGqflag(int gqflag) {
        this.gqflag = gqflag;
    }

    public int getDengji() {
        return dengji;
    }

    public void setDengji(int dengji) {
        this.dengji = dengji;
    }

    public IntentB_Q() {
        this.nd = "0";
        this.ndflag = 0;
        this.wd = "0";
        this.wdflag = 0;
        this.gq = "0";
        this.gqflag = 0;
        this.dengji = 0;
    }

    public void clear(){
        this.nd = "0";
        this.ndflag = 0;
        this.wd = "0";
        this.wdflag = 0;
        this.gq = "0";
        this.gqflag = 0;
        this.dengji = 0;
    }

}

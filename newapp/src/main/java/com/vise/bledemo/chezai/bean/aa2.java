package com.vise.bledemo.chezai.bean;

import com.hikvision.netsdk.NET_DVR_CONFIG;

public class aa2 extends NET_DVR_CONFIG {
    public int dwYear;
    public int dwMonth;
    public int dwDay;
    public int dwHour;
    public int dwMinute;
    public int dwSecond;

    public aa2(int dwYear, int dwMonth, int dwDay, int dwHour, int dwMinute, int dwSecond) {
        this.dwYear = dwYear;
        this.dwMonth = dwMonth;
        this.dwDay = dwDay;
        this.dwHour = dwHour;
        this.dwMinute = dwMinute;
        this.dwSecond = dwSecond;
    }

    public String ToString() {
        return this.dwYear + "/" + this.dwMonth + "/" + this.dwDay + " " + this.dwHour + ":" + this.dwMinute + ":" + this.dwSecond;
    }
}

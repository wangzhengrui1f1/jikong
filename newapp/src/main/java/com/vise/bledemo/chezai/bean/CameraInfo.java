package com.vise.bledemo.chezai.bean;


import com.sun.jna.NativeLong;

public class CameraInfo {
    private String address;
    private String userName;
    private String pwd;
    private short port;
    private NativeLong userId;
    private NativeLong channel;
    private NativeLong key;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public NativeLong getUserId() {
        return userId;
    }

    public void setUserId(NativeLong userId) {
        this.userId = userId;
    }

    public NativeLong getChannel() {
        return channel;
    }

    public void setChannel(NativeLong channel) {
        this.channel = channel;
    }

    public NativeLong getKey() {
        return key;
    }

    public void setKey(NativeLong key) {
        this.key = key;
    }

    // get set方法


}

package com.vise.bledemo.chezai.bean;

public class SxtBean {
    int iRealHandle;
    int iDataType;
    byte[] pDataBuffer;
    int iDataSize;

    public SxtBean(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
        this.iRealHandle = iRealHandle;
        this.iDataType = iDataType;
        this.pDataBuffer = pDataBuffer;
        this.iDataSize = iDataSize;
    }

    public int getiRealHandle() {
        return iRealHandle;
    }

    public void setiRealHandle(int iRealHandle) {
        this.iRealHandle = iRealHandle;
    }

    public int getiDataType() {
        return iDataType;
    }

    public void setiDataType(int iDataType) {
        this.iDataType = iDataType;
    }

    public byte[] getpDataBuffer() {
        return pDataBuffer;
    }

    public void setpDataBuffer(byte[] pDataBuffer) {
        this.pDataBuffer = pDataBuffer;
    }

    public int getiDataSize() {
        return iDataSize;
    }

    public void setiDataSize(int iDataSize) {
        this.iDataSize = iDataSize;
    }
}

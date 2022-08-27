package com.vise.bledemo.chezai.bean;

public class ShouDongQuZheng {
    int id;
    String shebeiname;
    String username;
    String type;//设备类型-车载
    String didian;
    String jingdu;
    String weidu;
    String pianyijingdu;
    String pianyiweidu;
    String nongdu;
    String wendu;
    String guangqiang;
    String chesu;
    String starttime;
    String endtime; //结束时间 = 事件发生时间
    String endtime2;//比较时间
    String beizhu;
    String ImagePath; //截图
    String ImagePath2; //拍照图片
    String grade;//1 2 3级别
    String flag;//1手动 2自动
    String uid; //唯一比较id用来对比是否上传过

    public String getShebeiname() {
        return shebeiname;
    }

    public void setShebeiname(String shebeiname) {
        this.shebeiname = shebeiname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDidian() {
        return didian;
    }

    public void setDidian(String didian) {
        this.didian = didian;
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

    public String getPianyijingdu() {
        return pianyijingdu;
    }

    public void setPianyijingdu(String pianyijingdu) {
        this.pianyijingdu = pianyijingdu;
    }

    public String getPianyiweidu() {
        return pianyiweidu;
    }

    public void setPianyiweidu(String pianyiweidu) {
        this.pianyiweidu = pianyiweidu;
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

    public String getChesu() {
        return chesu;
    }

    public void setChesu(String chesu) {
        this.chesu = chesu;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getEndtime2() {
        return endtime2;
    }

    public void setEndtime2(String endtime2) {
        this.endtime2 = endtime2;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImagePath2() {
        return ImagePath2;
    }

    public void setImagePath2(String imagePath2) {
        ImagePath2 = imagePath2;
    }

    @Override
    public String toString() {
        return "ShouDongQuZheng{" +
                "id=" + id +
                ", shebeiname='" + shebeiname + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", didian='" + didian + '\'' +
                ", jingdu='" + jingdu + '\'' +
                ", weidu='" + weidu + '\'' +
                ", pianyijingdu='" + pianyijingdu + '\'' +
                ", pianyiweidu='" + pianyiweidu + '\'' +
                ", nongdu='" + nongdu + '\'' +
                ", wendu='" + wendu + '\'' +
                ", guangqiang='" + guangqiang + '\'' +
                ", chesu='" + chesu + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", endtime2='" + endtime2 + '\'' +
                ", beizhu='" + beizhu + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                ", ImagePath2='" + ImagePath2 + '\'' +
                ", grade='" + grade + '\'' +
                ", flag='" + flag + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}

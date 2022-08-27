package com.vise.bledemo.chezai.bean;

public class ChezaiShiping {
    //车载上传视频  time 报警节点 flag是否上传成功
//    public static final String ChezaiShiping = "create table ChezaiShiping(id integer primary key autoincrement,path char(200)," +
//            "time char(200),flag char(20))" ;
    String path;
    String time;
    String flag;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "{" +
                "path='" + path + '\'' +
                ", time='" + time + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}

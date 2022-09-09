package com.vise.bledemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {
    //    String title;
//    String info;
//    String name;
//    String time;
//    int grade;//1 2 3级别
//    String locate;
//    String jingdu;
//    String weidu;
//    String nongdu;
//    String wendu;
//    String guangqiang;
//    String firsttime; //时间比较字段
//    String sid; //设备id
    //todo 报警信息
    public static final String baoJingData = "create table baoJingData(id integer primary key autoincrement,title char(100)," +
            "info char(200),name char(20)" +
            ",time char(30),grade integer,locate char(100),jingdu char(50),weidu char(50),nongdu char(10)" +
            ",wendu char(10),guangqiang char(10),firsttime char(10),sid char(25))";
    //
    //todo 操作
    public static final String caoZuo = "create table caoZuo(id integer primary key autoincrement,sbName char(100)," +
            "numvalues char(200),sId char(20)" +
            ",name char(30),time char(30),info char(100))";

    public static final String UserData = "create table UserData(id integer primary key autoincrement,username char(100)," +
            "password char(200),name char(20))";
    //todo 报警信息
    //int id;
    //    String shebeiname;
    //    String username;
    //    String type;//设备类型-车载
    //    String didian;
    //    String jingdu;
    //    String weidu;
    //    String pianyijingdu;
    //    String pianyiweidu;
    //    String nongdu;
    //    String wendu;
    //    String guangqiang;
    //    String chesu;
    //    String starttime;
    //    String endtime; //结束时间 = 事件发生时间
    //    String endtime2;//比较时间
    //    String beizhu;
    //    String ImagePath;
    //    String grade;//1 2 3级别
    //    String flag;//1手动 2自动
    //    int uid;
    public static final String CheZaiBaoJing = "create table CheZaiBaoJing(id integer primary key autoincrement,shebeiname char(50)," +
            "username char(10),type char(20)" + ",didian char(50),jingdu char(100),weidu char(100),pianyijingdu char(100),pianyiweidu char(100)" +
            ",nongdu char(30),wendu char(30),guangqiang char(30),chesu char(30),starttime char(50)" +
            ",endtime char(50),endtime2 char(50),beizhu char(100),ImagePath char(300),ImagePath2 char(300),grade char(5)" +
            ",flag char(5),uid integer)";


    private Context mContext;

    public MyDatabase(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(baoJingData);
        db.execSQL(caoZuo);
        db.execSQL(UserData);
        db.execSQL(CheZaiBaoJing);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }


}



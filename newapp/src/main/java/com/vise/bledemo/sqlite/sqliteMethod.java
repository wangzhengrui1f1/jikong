package com.vise.bledemo.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.model.GetJingbao;
import com.vise.bledemo.model.Jingbao;
import com.vise.bledemo.model.User;

import java.util.ArrayList;
import java.util.List;

public class sqliteMethod {
    //    String title;
//    String info;
//    String name;
//    String time;
//    int grade;//1 2 3级别
//    String locate;
//    String jingdu;
//    String weidu;
    //todo 添加报警信息
    public static void addData(MyDatabase myDatebaseHelper, Jingbao jingbao) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", jingbao.getTitle());
        values.put("info", jingbao.getInfo());
        values.put("name", jingbao.getName());
        values.put("time", jingbao.getTime());
        values.put("grade", String.valueOf(jingbao.getGrade()));
        values.put("locate", jingbao.getLocate());
        values.put("jingdu", jingbao.getJingdu());
        values.put("weidu", jingbao.getWeidu());
        values.put("nongdu", jingbao.getWeidu());
        values.put("wendu", jingbao.getWeidu());
        values.put("guangqiang", jingbao.getWeidu());
        values.put("firsttime", jingbao.getFirsttime());
        values.put("sid", jingbao.getSid());
        db.insert("baoJingData", null, values);
        db.close();
    }

    //todo 删除报警信息
    public void del(MyDatabase myDatebaseHelper, int id) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        db.delete("baoJingData", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //todo  修改
    private void less(MyDatabase myDatebaseHelper, Jingbao jingbao, int id) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", jingbao.getTitle());
        values.put("info", jingbao.getInfo());
        values.put("name", jingbao.getName());
        values.put("time", jingbao.getTime());
        values.put("grade", String.valueOf(jingbao.getGrade()));
        values.put("locate", jingbao.getLocate());
        values.put("jingdu", jingbao.getJingdu());
        values.put("weidu", jingbao.getWeidu());
        values.put("nongdu", jingbao.getWeidu());
        values.put("wendu", jingbao.getWeidu());
        values.put("guangqiang", jingbao.getWeidu());
        values.put("firsttime", jingbao.getFirsttime());
        values.put("sid", jingbao.getSid());
        db.update("baoJingData", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //todo 查询数据

    //    String title;
//    String info;
//    String name;
//    String time;
//    int grade;//1 2 3级别
//    String locate;
//    String jingdu;
//    String weidu;
    public static List<GetJingbao> getData(MyDatabase myDatebaseHelper) {
        List<GetJingbao> ls = new ArrayList<>();
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        Cursor cursor = db.query("baoJingData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            GetJingbao jingbao = new GetJingbao();
            jingbao.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jingbao.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            jingbao.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            jingbao.setName(cursor.getString(cursor.getColumnIndex("name")));
            jingbao.setTime(cursor.getString(cursor.getColumnIndex("time")));
            jingbao.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
            jingbao.setLocate(cursor.getString(cursor.getColumnIndex("locate")));
            jingbao.setJingdu(cursor.getString(cursor.getColumnIndex("jingdu")));
            jingbao.setWeidu(cursor.getString(cursor.getColumnIndex("weidu")));

            jingbao.setNongdu(cursor.getString(cursor.getColumnIndex("nongdu")));
            jingbao.setWendu(cursor.getString(cursor.getColumnIndex("wendu")));
            jingbao.setGuangqiang(cursor.getString(cursor.getColumnIndex("guangqiang")));
            jingbao.setFirsttime(cursor.getString(cursor.getColumnIndex("firsttime")));
            jingbao.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            ls.add(jingbao);
        }
        return ls;
    }

    //todo 查询数据
    public static List<GetJingbao> GetJingbaoData(MyDatabase myDatebaseHelper,
                                                  String starttime, String endtime, String sid, String name) {
        int start = Integer.valueOf(starttime);
        int end = Integer.valueOf(endtime);
        List<GetJingbao> ls = new ArrayList<>();
        ls.clear();
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        Cursor cursor = db.query("baoJingData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            GetJingbao jingbao = new GetJingbao();
            jingbao.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jingbao.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            jingbao.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            jingbao.setName(cursor.getString(cursor.getColumnIndex("name")));
            jingbao.setTime(cursor.getString(cursor.getColumnIndex("time")));
            jingbao.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
            jingbao.setLocate(cursor.getString(cursor.getColumnIndex("locate")));
            jingbao.setJingdu(cursor.getString(cursor.getColumnIndex("jingdu")));
            jingbao.setWeidu(cursor.getString(cursor.getColumnIndex("weidu")));

            jingbao.setNongdu(cursor.getString(cursor.getColumnIndex("nongdu")));
            jingbao.setWendu(cursor.getString(cursor.getColumnIndex("wendu")));
            jingbao.setGuangqiang(cursor.getString(cursor.getColumnIndex("guangqiang")));
            jingbao.setFirsttime(cursor.getString(cursor.getColumnIndex("firsttime")));
            jingbao.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            if ((Integer.valueOf(jingbao.getFirsttime()) >= start && Integer.valueOf(jingbao.getFirsttime()) <= end)
                    && jingbao.getSid().equals(sid) && jingbao.getName().equals(name)) {
                ls.add(jingbao);
            }

        }
        return ls;
    }

    //todo 添加操作信息
    public static void addCaozuoData(MyDatabase myDatebaseHelper, Jingbao jingbao) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", jingbao.getTitle());
        values.put("info", jingbao.getInfo());
        values.put("name", jingbao.getName());
        values.put("time", jingbao.getTime());
        values.put("grade", String.valueOf(jingbao.getGrade()));
        values.put("locate", jingbao.getLocate());
        values.put("jingdu", jingbao.getJingdu());
        values.put("weidu", jingbao.getWeidu());
        db.insert("baoJingData", null, values);
        db.close();
    }

    //todo 添加用户
    public static void addUser(MyDatabase myDatebaseHelper, User user) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        db.insert("UserData", null, values);
        db.close();
    }

    //todo 登陆
    public static boolean IScheck(MyDatabase myDatebaseHelper, String passwords) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        String Query = "Select * from UserData where password=?";
        Cursor cursor = db.rawQuery(Query, new String[]{passwords});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    //todo 添加车载报警信息
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
    public static void addCheZaiBaoJingData(MyDatabase myDatebaseHelper, ShouDongQuZheng jingbao) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("shebeiname", jingbao.getShebeiname());
        values.put("username", jingbao.getUsername());
        values.put("type", jingbao.getType());
        values.put("didian", jingbao.getDidian());
        values.put("jingdu", jingbao.getJingdu());
        values.put("weidu", jingbao.getWeidu());
        values.put("pianyijingdu", jingbao.getPianyijingdu());
        values.put("pianyiweidu", jingbao.getPianyiweidu());
        values.put("nongdu", jingbao.getNongdu());
        values.put("wendu", jingbao.getWendu());
        values.put("guangqiang", jingbao.getGuangqiang());
        values.put("chesu", jingbao.getChesu());
        values.put("starttime", jingbao.getStarttime());
        values.put("endtime", jingbao.getEndtime());
        values.put("endtime2", jingbao.getEndtime2());
        values.put("beizhu", jingbao.getBeizhu());
        values.put("ImagePath", jingbao.getImagePath());
        values.put("ImagePath2", jingbao.getImagePath2());
        values.put("grade", jingbao.getGrade());
        values.put("flag", jingbao.getFlag());
        values.put("uid", jingbao.getUid());
        db.insert("CheZaiBaoJing", null, values);
        db.close();
    }

    //todo 查询车载记录数据 starttime =-1 全部
    public static List<ShouDongQuZheng> getCheZaiBaoJing(MyDatabase myDatebaseHelper,
                                                         String starttime, String endtime, String shebeiname, String username) {
        int start = Integer.valueOf(starttime);
        int end = Integer.valueOf(endtime);
        int flag = 1;//0不添加 1添加
        List<ShouDongQuZheng> ls = new ArrayList<>();
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
//        Cursor cursor = db.query("CheZaiBaoJing", null, null,
//                null, null, null, null);
//        String Query = "select * from CheZaiBaoJing where username like '%9%' order by id desc LIMIT (0 * 3), 3";
        String Query = "select * from CheZaiBaoJing order by id desc";
        Cursor cursor = db.rawQuery(Query, null);
        while (cursor.moveToNext()) {
            //todo 添加车载报警信息
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
            ShouDongQuZheng jingbao = new ShouDongQuZheng();
            jingbao.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jingbao.setShebeiname(cursor.getString(cursor.getColumnIndex("shebeiname")));
            jingbao.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            jingbao.setType(cursor.getString(cursor.getColumnIndex("type")));
            jingbao.setDidian(cursor.getString(cursor.getColumnIndex("didian")));
            jingbao.setJingdu(cursor.getString(cursor.getColumnIndex("jingdu")));
            jingbao.setWeidu(cursor.getString(cursor.getColumnIndex("weidu")));
            jingbao.setPianyijingdu(cursor.getString(cursor.getColumnIndex("pianyijingdu")));
            jingbao.setPianyiweidu(cursor.getString(cursor.getColumnIndex("pianyiweidu")));

            jingbao.setNongdu(cursor.getString(cursor.getColumnIndex("nongdu")));
            jingbao.setWendu(cursor.getString(cursor.getColumnIndex("wendu")));
            jingbao.setGuangqiang(cursor.getString(cursor.getColumnIndex("guangqiang")));
            jingbao.setChesu(cursor.getString(cursor.getColumnIndex("chesu")));
            jingbao.setStarttime(cursor.getString(cursor.getColumnIndex("starttime")));
            jingbao.setEndtime(cursor.getString(cursor.getColumnIndex("endtime")));
            jingbao.setEndtime2(cursor.getString(cursor.getColumnIndex("endtime2")));
            jingbao.setBeizhu(cursor.getString(cursor.getColumnIndex("beizhu")));
            jingbao.setImagePath(cursor.getString(cursor.getColumnIndex("ImagePath")));
            jingbao.setImagePath2(cursor.getString(cursor.getColumnIndex("ImagePath2")));
            jingbao.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            jingbao.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            jingbao.setUid(cursor.getString(cursor.getColumnIndex("uid")));


            //todo
            if (!shebeiname.equals("-1") && !shebeiname.equals("")) {
                if (jingbao.getShebeiname().contains(shebeiname)) {
                    Log.e("ttatta-是", jingbao.getShebeiname());
                } else {
                    flag = 0;
//                    Log.e("ttatta-查询操作人员1",jingbao.getUsername());
//                    Log.e("ttatta-查询操作人员2",username);
                }
            }
            if (!username.equals("-1") && !username.equals("")) {
                if (jingbao.getUsername().contains(username)) {
                    Log.e("ttatta-是", jingbao.getUsername());
                } else {
                    flag = 0;
//                    Log.e("ttatta-查询操作人员1",jingbao.getUsername());
//                    Log.e("ttatta-查询操作人员2",username);
                }
            }

            if (!endtime.equals("-1") && !endtime.equals("")) {
                if ((Integer.valueOf(jingbao.getEndtime2()) >= start && Integer.valueOf(jingbao.getEndtime2()) <= end)) {

                } else {
                    flag = 0;
                }
            }
//            if(Integer.valueOf(starttime)>100) {
//                if ((Integer.valueOf(jingbao.getEndtime2()) >= start && Integer.valueOf(jingbao.getEndtime2()) <= end)) {
//
//                }else {
//                    flag =0;
//                }
//            }
            Log.e("ttatta-flag", String.valueOf(flag));
            if (flag == 1) {
                ls.add(jingbao);
            }
            flag = 1;
        }
        return ls;
    }

    //todo 删除报警信息
    public static void dels(MyDatabase myDatebaseHelper, List<Integer> ls) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        for (int i = 0; i < ls.size(); i++) {
            db.delete("CheZaiBaoJing", "id=?", new String[]{String.valueOf(ls.get(i))});
        }
        db.close();
    }


    //todo  验证上传成功后修改Uid
    public static void lessUid(MyDatabase myDatebaseHelper, String uid, int id) {
        SQLiteDatabase db = myDatebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        db.update("CheZaiBaoJing", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

}

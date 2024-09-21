package com.example.electricguitar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDAO {
    private SQLiteDatabase myDb;  //类的成员
    private DatabeasHaper dbHelper;  //类的成员

    public MyDAO(Context context) {

        dbHelper = new DatabeasHaper(context, "test.db", null, 1);
    }

    public Cursor allQuery() {    //查询所有已发布的音谱记录
        myDb = dbHelper.getReadableDatabase();
        return myDb.rawQuery("select * from information where flag=0", null);
    }
    public Cursor query2(String s){
        myDb = dbHelper.getReadableDatabase();
        return myDb.rawQuery("select * from information where title like ?",new String[]{"%" +s+"%" });
    }
    public Cursor query3(String s){
        myDb = dbHelper.getReadableDatabase();
        return myDb.rawQuery("select * from information where account == ?",new String[]{s });
    }

    public int getRecordsNumber() {  //返回数据表记录数
        myDb = dbHelper.getReadableDatabase();
        Cursor cursor = myDb.rawQuery("select * from user", null);
        return cursor.getCount();
    }

    public void insertInfo(String account, String password) {  //用户注册信息插入记录
        myDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account",account);
        values.put("password", password);
        long rowid = myDb.insert(DatabeasHaper.TB_NAME, null, values);
        if (rowid == -1)
            Log.i("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功！" + rowid);
    }
    public void insertInfomation(String account, String title,String information) {  //用户保存信息插入记录
        myDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account",account);
        values.put("title", title);
        values.put("inform",information);
        values.put("flag",1);
        long rowid = myDb.insert(DatabeasHaper.tb_name, null, values);
        if (rowid == -1)
            Log.i("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功！" + rowid);
    }
    public void update(String id,String title,String information ){//更新乐谱信息
        myDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("inform", information);
        String where = "id=" +id;
        int i = myDb.update(DatabeasHaper.tb_name, values, where, null);


        if (i > 0)
            Log.i("myDbDemo", "数据更新成功！");
        else
            Log.i("myDbDemo", "数据未更新！");

    }
    public void delete(String id){//删除乐谱信息
        myDb = dbHelper.getWritableDatabase();
        String where = "id=" +id;
        int i = myDb.delete(DatabeasHaper.tb_name, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据删除成功！");
        else
            Log.i("myDbDemo", "数据未删除！");
    }
    public  void releas(String id){
        myDb = dbHelper.getWritableDatabase();
        String where = "id=" +id;
        ContentValues values=new ContentValues();
        values.put("flag",0);
        int i = myDb.update(DatabeasHaper.tb_name, values, where, null);


        if (i > 0)
            Log.i("myDbDemo", "乐谱发布成功！");
        else
            Log.i("myDbDemo", "乐谱发布失败！");
    }
    public String query(String account){//登陆时验证账号密码是否匹配
        SQLiteDatabase myDb = dbHelper.getReadableDatabase();

        Cursor cursor = myDb.rawQuery("SELECT * FROM user WHERE account = ?", new String[]{account});

        if(cursor.moveToFirst()){
            @SuppressLint("Range")
            String p = cursor.getString(cursor.getColumnIndex("password"));

            return p;

        } else {

            return account;
        }
    }

    public int Query(String account){//查询用户名是否已被注册
        SQLiteDatabase myDb = dbHelper.getReadableDatabase();

        Cursor cursor = myDb.rawQuery("SELECT * FROM user WHERE account = ?", new String[]{account});
        if(cursor.moveToFirst()){
            return 0;
        }
        return -1;
    }

}

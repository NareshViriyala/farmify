package com.example.nareshviriyala.farmifyagentfarmer.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.Settings;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "farmifyAgent";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_systemparameter (id INTEGER PRIMARY KEY AUTOINCREMENT, paramname text, paramvalue text)");
        /*Set basic system parameters*/
        db.execSQL("INSERT INTO tbl_systemparameter(paramname, paramvalue) VALUES('device_id','"+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+"')");
        db.execSQL("INSERT INTO tbl_systemparameter(paramname, paramvalue) VALUES('device_type','1')");
        db.execSQL("INSERT INTO tbl_systemparameter(paramname, paramvalue) VALUES('device_version','"+Build.VERSION.RELEASE+"')");
        db.execSQL("INSERT INTO tbl_systemparameter(paramname, paramvalue) VALUES('app_version','0.0.0.0')");
    }

    public void setParameter(String paramname, String paramvalue){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("paramname", paramname);
            contentValues.put("paramvalue", paramvalue);
            db.insertWithOnConflict("tbl_systemparameter","paramname",contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        catch (Exception e){}
        finally {
            db.close();
        }
    }

    public String getParameter(String paramname){
        SQLiteDatabase db = this.getReadableDatabase();
        String paramvalue = "";
        Cursor res =  db.rawQuery("Select * from tbl_systemparameter where paramname = '"+paramname+"'", null);
        try {
            if(res.moveToFirst()) {
                do {
                    paramvalue = res.getString(res.getColumnIndex("paramvalue"));
                }while (res.moveToNext());
            }
        }
        catch (Exception e){}
        finally {
            res.close();
            db.close();
        }
        return paramvalue;
    }

    public void deleteParameter(String paramname){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_systemparameter", "paramname='"+paramname+"'",  null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



























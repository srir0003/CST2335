package com.example.androidlabs;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper
{

    public final static String DATABASE_NAME = "MyDatabaseFile";
    public final static int VERSION_NUM = 2;
    public final static String TABLE_NAME = "ChatTable";
    public final static String COL_ID = "id";
    public final static String COL_MESSAGE = "message";
    public final static String COL_ISSEND = "isSent";
    public static final String COL_RECEIVED = "isReceived";


    public MyDatabaseOpenHelper(Activity ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "

                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_MESSAGE + " TEXT, "
                + COL_ISSEND +" TEXT, "
                + COL_RECEIVED +" TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

}

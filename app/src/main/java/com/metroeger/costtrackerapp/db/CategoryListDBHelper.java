package com.metroeger.costtrackerapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CategoryListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "categories_db";
    private static final int DATABASE_VERSION = 1;

    public CategoryListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Date today = new Date();
        SimpleDateFormat print = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
        String strDate = print.format(today);

        db.execSQL("CREATE TABLE categoryitem(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount NUMERIC)");
        db.execSQL("INSERT INTO categoryitem (name,amount) VALUES ('Food', 0)");
        db.execSQL("INSERT INTO categoryitem (name,amount) VALUES ('Travel', 0)");
        db.execSQL("INSERT INTO categoryitem (name,amount) VALUES ('Bills', 0)");
        db.execSQL("INSERT INTO categoryitem (name,amount) VALUES ('Shopping', 0)");
        db.execSQL("INSERT INTO categoryitem (name,amount) VALUES ('Other', 0)");

        db.execSQL("CREATE TABLE item(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, date TEXT, " +
                "amount NUMERIC, " +
                "categoryID INTEGER NOT NULL, " +
                "FOREIGN KEY(categoryID) REFERENCES categoryitem(id))");
        db.execSQL("INSERT INTO item (categoryID, name, date, amount) VALUES (1, 'some food', ?, 3333)", new String[]{strDate});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE categoryitem");
            db.execSQL("DROP TABLE item");
            onCreate(db);
    }
}

package com.metroeger.costtrackerapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.metroeger.costtrackerapp.model.CategoryItem;
import com.metroeger.costtrackerapp.model.Item;

public class CategoryItemDAO {

    private CategoryListDBHelper helper;
    private Map<Integer, CategoryItem> categoryMap = new HashMap<>();


    public CategoryItemDAO(Context context) {
        helper = new CategoryListDBHelper(context);
    }

    public List<CategoryItem> getAllCategoryItems() {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *FROM categoryitem", null);

        List<CategoryItem> categoryItems = new ArrayList<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String am = cursor.getString(cursor.getColumnIndex("amount"));
            double amount = Double.parseDouble(am);

            CategoryItem categoryItem = new CategoryItem(id, name, amount);
            categoryItems.add(categoryItem);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        for (CategoryItem ci : categoryItems) {
            categoryMap.put(ci.getId(), ci);
        }

        return categoryItems;
    }

    public List<Item> getAllItems(CategoryItem ci) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM item WHERE categoryID=?", new String[]{ci.getId() + ""});

        List<Item> items = new ArrayList<>();

        cursor.moveToFirst();

        try {

            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String am = cursor.getString(cursor.getColumnIndex("amount"));
                double amount = Double.parseDouble(am);
                String d = cursor.getString(cursor.getColumnIndex("date"));
                Date date = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH).parse(d);

                items.add(new Item(id, ci, name, date, amount));

                cursor.moveToNext();
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public void deleteCategoryItem(CategoryItem ci) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("categoryitem", "id=?", new String[]{ci.getId() + ""});
        db.close();
    }

    public void saveCategoryItem(CategoryItem ci) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("name", ci.getName());
        contentValue.put("amount", ci.getAmount());

        if (ci.getId() == -1) {
            long id = db.insert("categoryitem", null, contentValue);
            ci.setId((int) id);
        } else {
            db.update("categoryitem", contentValue, "id=?", new String[]{ci.getId() + ""});
        }
        db.close();
    }

    public void deleteAllCategories() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM categoryitem");
        db.close();
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("item", "id=?", new String[]{item.getId() + ""});
        db.close();
    }

    public void saveItem(Item item) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put("categoryID", item.getCategoryItem().getId());
        contentValue.put("name", item.getName());
        contentValue.put("date", item.getDate()+"");
        contentValue.put("amount", item.getAmount());


        if (item.getId() == -1) {
            long id = db.insert("item", null, contentValue);
            item.setId((int) id);
        } else {
            db.update("item", contentValue, "id=?", new String[]{item.getId() + ""});
        }
        db.close();
    }

    public void deleteAllItems(CategoryItem ci) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM item WHERE categoryID=?", new String[]{ci.getId() + ""});
        db.close();
    }

    public Map<Integer, CategoryItem> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<Integer, CategoryItem> categoryMap) {
        this.categoryMap = categoryMap;
    }
}

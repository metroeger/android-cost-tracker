package com.metroeger.costtrackerapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item implements Serializable {

    private int id;
    private CategoryItem categoryItem;
    private String name;
    private double amount;
    private Date date;

    public Item(){
        id = -1;
    }

    public Item(int id, CategoryItem categoryItem, String name, Date date, double amount) {
        this.id = id;
        this.categoryItem = categoryItem;
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public Item(int id, CategoryItem categoryItem, String name, double amount) {
        this.id = id;
        this.categoryItem = categoryItem;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryItem getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(CategoryItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}

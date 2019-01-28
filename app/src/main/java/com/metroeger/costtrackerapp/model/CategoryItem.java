package com.metroeger.costtrackerapp.model;

import java.io.Serializable;
import java.util.List;

public class CategoryItem implements Serializable {

    private int id;
    private String name;
    private double amount;

    public CategoryItem(){
        id = -1;
        amount=0;
    }

    public CategoryItem(String name, double amount) {
        id = -1;
        this.name = name;
        this.amount = amount;
    }

    public CategoryItem(String name) {
        id = -1;
        this.name = name;
        amount = 0;
    }

    public CategoryItem(int id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAmount(List<Item> items) {
        for (Item it : items){
            amount += it.getAmount();
        }
    }
    public void setAmount(double amount) {
       this.amount = amount;
    }
}

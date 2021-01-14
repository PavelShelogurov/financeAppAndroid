package com.example.myfinance.utils;

public class DataStoreStatistics extends DataStoreParent {

    private int earned;
    private int spent;
    private String date;

    public DataStoreStatistics(int earned, int spent, int month, int year){
        this.earned = earned;
        this.spent = spent;
        this.date = super.dateStringFormatMMYYYY(month, year);
    }

    public int getEarned() {
        return earned;
    }

    public int getSpent() {
        return spent;
    }

    public String getDate() {
        return date;
    }

}

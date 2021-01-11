package com.example.myfinance.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataProvider {
    private static final String DARA_FORMAT = "dd.MM.yyyy";
    private static final String DATA_SPLIT_REGEX = "\\.";

    private int day;
    private int month;
    private int year;

    public void generate(){
        Date data = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DARA_FORMAT);
        String[] dataArray = simpleDateFormat.format(data).split(DATA_SPLIT_REGEX);
        day = Integer.parseInt(dataArray [0]);
        month = Integer.parseInt(dataArray[1]);
        year = Integer.parseInt(dataArray[2]);
    }

    public int getDay(){
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

}
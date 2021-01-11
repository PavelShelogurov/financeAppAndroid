package com.example.myfinance.utils;

public class DataStore {

    private int earned;
    private int spent;
    private String date;

    //для проверки текущий месяц или нет
    private DataProvider dataProvider = new DataProvider();

    public DataStore(int earned, int spent, int month, int year){
        this.earned = earned;
        this.spent = spent;
        this.date = dateToString(month, year);
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

    private String dateToString(int month, int year){
        String date = "";
        switch (month){
            case 1:
                date = "янв. ";
                break;
            case 2:
                date = "фев. ";
                break;
            case 3:
                date = "март ";
                break;
            case 4:
                date = "апр. ";
                break;
            case 5:
                date = "май ";
                break;
            case 6:
                date = "июнь ";
                break;
            case 7:
                date = "июль ";
                break;
            case 8:
                date = "авг. ";
                break;
            case 9:
                date = "сент. ";
                break;
            case 10:
                date = "окт. ";
                break;
            case 11:
                date = "нояб. ";
                break;
            case 12:
                date = "дек. ";
                break;
            default:date = "error";
        }
        return date.concat(String.valueOf(year));
    }



}

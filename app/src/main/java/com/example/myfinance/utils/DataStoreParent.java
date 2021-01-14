package com.example.myfinance.utils;

public class DataStoreParent {
    private static final String SPACE = " ";

    protected String convertNumberOfMonthIntoText(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                monthString = "янв. ";
                break;
            case 2:
                monthString = "фев. ";
                break;
            case 3:
                monthString = "март ";
                break;
            case 4:
                monthString = "апр. ";
                break;
            case 5:
                monthString = "май ";
                break;
            case 6:
                monthString = "июнь ";
                break;
            case 7:
                monthString = "июль ";
                break;
            case 8:
                monthString = "авг. ";
                break;
            case 9:
                monthString = "сент. ";
                break;
            case 10:
                monthString = "окт. ";
                break;
            case 11:
                monthString = "нояб. ";
                break;
            case 12:
                monthString = "дек. ";
                break;
            default:
                monthString = "error";
        }
        return monthString;
    }

    protected String dateStringFormatMMYYYY(int month, int year){
        String date = convertNumberOfMonthIntoText(month);

        return date.concat(String.valueOf(year));
    }

    protected String dateStringFormatDDMM(int day, int month){
        String dateDDMM = String.valueOf(day);
        return dateDDMM.concat(SPACE + convertNumberOfMonthIntoText(month));
    }

}

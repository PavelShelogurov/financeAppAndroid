package com.example.myfinance.utils;

public class DataSpentDetailing extends DataStoreParent {
    /**
     * Класс для хранения информации за одну ОТДЕЛЬНУЮ трату
     */
    private int spent;
    private String spentComment;
    private String dateDDMM;

    public DataSpentDetailing(int spent, int day, int month, int year, String spentComment){
        this.spent = spent;
        this.spentComment = spentComment;
        this.dateDDMM = super.dateStringFormatDDMM(day, month);
    }

    public int getSpent() {
        return spent;
    }

    public String getDateDDMM(){
        return dateDDMM;
    }

    public String getSpentComment(){
        return spentComment;
    }

}

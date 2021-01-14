package com.example.myfinance.utils;

import java.util.ArrayList;
import java.util.List;

public class DataSpentInMonth extends DataStoreParent {
    /**
     * Класс хранения информации за все траты в месяце
     */

    //тут храниться за какой месяц какого года
    private String dateMMYYY;
    //лист со всеми тратами за указанный месяц
    private List<DataSpentDetailing> spentDetailingList = new ArrayList<>();

    public DataSpentInMonth(int month, int year, List<DataSpentDetailing> dataSpentDetailings) {
        this.dateMMYYY = super.dateStringFormatMMYYYY(month, year);
        spentDetailingList.addAll(dataSpentDetailings);
    }

    public void addSpentInfo(DataSpentDetailing dataSpentDetailing) {
        spentDetailingList.add(dataSpentDetailing);
    }

    public String getDateMMYYY(){
        return dateMMYYY;
    }

    public List<DataSpentDetailing> getSpentDetailingList() {
        return spentDetailingList;
    }
}

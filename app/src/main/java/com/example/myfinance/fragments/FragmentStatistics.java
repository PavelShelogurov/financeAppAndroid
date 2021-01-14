package com.example.myfinance.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfinance.MainActivity;
import com.example.myfinance.R;
import com.example.myfinance.database.DataBaseHelper;
import com.example.myfinance.database.DataBaseSQLQuery;
import com.example.myfinance.dialog_fragments.ErrorDialogFragment;
import com.example.myfinance.utils.DataProvider;
import com.example.myfinance.utils.DataStoreStatistics;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatistics extends Fragment {
    private final static String EMPTY_STRING = "";


    TextView textEarnedField;
    TextView textSpentField;
    TextView textEarned;
    TextView textSpent;
    AutoCompleteTextView monthList;
    ImageView rubleEarned;
    ImageView rubleSpent;

    public FragmentStatistics() {
        super(R.layout.statictics_fragment);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textEarnedField = view.findViewById(R.id.text_output_earned);
        textSpentField = view.findViewById(R.id.text_output_spent);
        textEarned = view.findViewById(R.id.text_earted);
        textSpent = view.findViewById(R.id.text_spent);
        monthList = view.findViewById(R.id.month_list);
        rubleEarned = view.findViewById(R.id.ruble_earned);
        rubleSpent = view.findViewById(R.id.ruble_spent);

        //пока не выбран период отображения данных, скрываем все элементы из видимости
        rubleEarned.setImageDrawable(null);
        rubleSpent.setImageDrawable(null);
        textEarnedField.setText(EMPTY_STRING);
        textSpentField.setText(EMPTY_STRING);
        textEarned.setText(R.string.choose_period);
        textSpent.setText(EMPTY_STRING);


        List<DataStoreStatistics> dataStoreStatisticsList = searchUniqueDataStoreInBD(MainActivity.getDataBaseHelper(), MainActivity.getDataBase());
        //елси dataStoreList.size()==0 то БД пустая
        if(dataStoreStatisticsList.size()==0) {
            AlertDialog dialog = ErrorDialogFragment.getAlertDialogEmptyDB(getActivity());
            dialog.show();
            //если база данных пустая
            textEarned.setText(R.string.error_empty_data_base);

        } else{
            //создаём меню выбора месяцев
            String[] menuItems = new String[dataStoreStatisticsList.size()];
            for (int i = 0; i < menuItems.length; i++) {
                menuItems[i] = dataStoreStatisticsList.get(i).getDate();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, menuItems);
            monthList.setAdapter(adapter);
            monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //возобнавляем видимость элементов
                    textEarned.setText(R.string.earned);
                    textSpent.setText(R.string.spent);
                    rubleEarned.setImageResource(R.drawable.ruble);
                    rubleSpent.setImageResource(R.drawable.ruble);


                    textEarnedField.setText(String.valueOf(dataStoreStatisticsList.get(position).getEarned()));
                    textSpentField.setText(String.valueOf(dataStoreStatisticsList.get(position).getSpent()));
                }
            });

        }



    }

    //метод возвращиюий сумму трат или заработка за определённый месяц и определённый год

    /**
     * @param month
     * @param year
     * @param earnedOrSpent  с качестве этого параметра передавить DataBaseHelper.COLUMN_EARNED (если нужно плучить заработанные деньги)
     *                       или DataBaseHelper.COLUMN_SPENT (если нужно получить потраченные деньги)
     * @param dataBaseHelper MainActivity.getDataBaseHelper() (экземпляр DataBaseHelper из маин класса, чтобы не создавать много копий)
     * @param dataBase
     * @return
     */
    private int getEarnedOrSpentAmount(int month, int year, String earnedOrSpent, DataBaseHelper dataBaseHelper, SQLiteDatabase dataBase) {
        //создаётся устовие для SQL запроса в базу данных (where)
        String where = DataBaseHelper.COLUMN_MONTH + " = " + month +
                " and " + DataBaseHelper.COLUMN_YEAR + " = " + year;

        dataBase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = null;
        //имя колонки в БД по который будет происходит суммирование
        String columnName = null;
        //в зависимости от того какую колонку нужно получить (траты или заработок) генерируется нужный SQL запрос
        if (earnedOrSpent.equalsIgnoreCase(DataBaseHelper.COLUMN_EARNED)) {
            cursor = dataBase.query(DataBaseHelper.TABLE_MAIN, new String[]{"sum (" + DataBaseHelper.COLUMN_EARNED + ")"}, where, null, null, null, null);
            columnName = DataBaseHelper.COLUMN_EARNED;

        } else if (earnedOrSpent.equalsIgnoreCase(DataBaseHelper.COLUMN_SPENT)) {
            cursor = dataBase.query(DataBaseHelper.TABLE_MAIN, new String[]{"sum (" + DataBaseHelper.COLUMN_SPENT + ")"}, where, null, null, null, null);
            columnName = DataBaseHelper.COLUMN_SPENT;

        } else {
            Log.d("mas", "В метод getEarnedOrSpentAmount передана неправильная строка не earned и не spent");
        }

        cursor.moveToFirst();
        //после SQL зпароса поялвяется колонка sum (columnName), показывающая сумму затрат/трат за текущи месяц в году
        int amount = cursor.getInt(cursor.getColumnIndex("sum (" + columnName + ")"));

        return amount;
    }

    //метод который ищет в базе данный сумму затрат и зароботка для кажого месяца в каждом году и возвращает это в виде объектов DataStore, способных всё это хранить
    private List<DataStoreStatistics> searchUniqueDataStoreInBD(DataBaseHelper dataBaseHelper, SQLiteDatabase dataBase) {
        final String MIN = "MIN";
        final String MAX = "MAX";

        dataBase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = null;

        //объект, помогающий потределять текущаю дату
        DataProvider dataProvider = new DataProvider();
        dataProvider.generate();
        //текущий год будет максимальным в базе данных
        int currentYear = dataProvider.getYear();
        //поиск минимального года в базе данных
        int minYearInDB = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MIN, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_YEAR, null, dataBase);

        List<DataStoreStatistics> dataStoreStatistics = new ArrayList<>();
        //переменная minYearInDB может равняться нулю, если от базы данных пришёл пустой ответ
        //значит БД пустая и только завтель ещё не пользовался приложением
        if(minYearInDB!=0) {
            for (int year = currentYear; year >= minYearInDB; year--) {
                String where = DataBaseHelper.COLUMN_YEAR + " = " + year;
                int maxMonth = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MAX, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_MONTH, where, dataBase);
                int minMonth = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MIN, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_MONTH, where, dataBase);
                for (int month = maxMonth; month >= minMonth; month--) {
                    int earnedAmount = getEarnedOrSpentAmount(month, year, DataBaseHelper.COLUMN_EARNED, dataBaseHelper, dataBase);
                    int spentAmount = getEarnedOrSpentAmount(month, year, DataBaseHelper.COLUMN_SPENT, dataBaseHelper, dataBase);
                    dataStoreStatistics.add(new DataStoreStatistics(earnedAmount, spentAmount, month, year));
                }
            }
        }

        return dataStoreStatistics;
    }
    /*
    //метод который ищет максимальный или минимальный элемент в определённом столбце
    private int searchMinOrMaxIntValueOfColumnInDB (String minOrMax, String tableName, String columnName, String where, SQLiteDatabase database){
        Cursor cursor = database.query(tableName, new String[] {minOrMax + " (" + columnName + ")"}, where, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(minOrMax + " (" + columnName + ")"));
    }

     */

}

package com.example.myfinance.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.myfinance.utils.DataSpentDetailing;
import com.example.myfinance.utils.DataSpentInMonth;
import com.example.myfinance.utils.DataStoreParent;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetailingOfExpense extends Fragment {


    ListView detailingOfExpenseList;
    AutoCompleteTextView monthList;

    public FragmentDetailingOfExpense() {
        super(R.layout.detailing_of_expense_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monthList = view.findViewById(R.id.month_list_spent_detailing);
        detailingOfExpenseList = view.findViewById(R.id.detailing_spent_list);


        List<DataSpentInMonth> dataSpentInMonths = findSpentForEveryMonth(MainActivity.getDataBaseHelper(), MainActivity.getDataBase());
        if (dataSpentInMonths.size() == 0) {
            AlertDialog dialog = ErrorDialogFragment.getAlertDialogEmptyDB(getActivity());
            dialog.show();

        } else {

            String[] menu = new String[dataSpentInMonths.size()];
            for (int i = 0; i < dataSpentInMonths.size(); i++) {
                menu[i] = dataSpentInMonths.get(i).getDateMMYYY();
            }
            ArrayAdapter<String> adapterMenu = new ArrayAdapter<>(getContext(), R.layout.list_item, menu);
            monthList.setAdapter(adapterMenu);

            monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<DataSpentDetailing> listSpentDetailingInMonth = dataSpentInMonths.get(position).getSpentDetailingList();
                    int[] spent = new int[listSpentDetailingInMonth.size()];
                    String[] dateDDMM = new String[listSpentDetailingInMonth.size()];
                    String[] spentComment = new String[listSpentDetailingInMonth.size()];
                    for (int i = 0; i < listSpentDetailingInMonth.size(); i++) {
                        spent[i] = listSpentDetailingInMonth.get(listSpentDetailingInMonth.size() - 1 - i).getSpent();
                        dateDDMM[i] = listSpentDetailingInMonth.get(listSpentDetailingInMonth.size() - 1 - i).getDateDDMM();
                        spentComment[i] = listSpentDetailingInMonth.get(listSpentDetailingInMonth.size() - 1 - i).getSpentComment();
                    }
                    MyAdapterForDetalingExpenseList adapter = new MyAdapterForDetalingExpenseList(getContext(), dateDDMM, spent, spentComment);
                    detailingOfExpenseList.setAdapter(adapter);

                }
            });
        }


    }

    //метод ищет все затрыты за каждый месяц
    private List<DataSpentInMonth> findSpentForEveryMonth(DataBaseHelper dataBaseHelper, SQLiteDatabase dataBase) {
        String MAX = "MAX";
        String MIN = "MIN";
        List<DataSpentInMonth> listSpentInMonth = new ArrayList<>();
        dataBase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = null;

        //объект, помогающий потределять текущаю дату
        DataProvider dataProvider = new DataProvider();
        dataProvider.generate();
        //текущий год будет максимальным в базе данных
        int currentYear = dataProvider.getYear();
        //поиск минимального года в базе данных
        int minYearInDB = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MIN, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_YEAR, null, dataBase);
        Log.d("mas", "currentYear = " + currentYear + " minYearInDB = " + minYearInDB);
        if (minYearInDB != 0) {
            for (int year = currentYear; year >= minYearInDB; year--) {
                String where = DataBaseHelper.COLUMN_YEAR + " = " + year;
                int maxMonth = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MAX, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_MONTH, where, dataBase);
                int minMonth = DataBaseSQLQuery.searchMinOrMaxIntValueOfColumnInDB(MIN, DataBaseHelper.TABLE_MAIN, DataBaseHelper.COLUMN_MONTH, where, dataBase);
                Log.d("mas", "year = " + year + " maxMonth = " + maxMonth + " minMonth " + minMonth);
                for (int month = maxMonth; month >= minMonth; month--) {
                    listSpentInMonth.add(new DataSpentInMonth(month, year, findAllSpentInMonth(month, year, dataBase)));
                }
            }
        }

        return listSpentInMonth;
    }

    //писк всех трат за определённый месяц в году
    private List<DataSpentDetailing> findAllSpentInMonth(int month, int year, SQLiteDatabase dataBase) {

        List<DataSpentDetailing> listOfSpentInMonth = new ArrayList<>();
        String where = DataBaseHelper.COLUMN_MONTH + " = " + month + " and " + DataBaseHelper.COLUMN_YEAR + " = " + year + " and " + DataBaseHelper.COLUMN_SPENT + " > 0";
        Cursor cursor = dataBase.query(DataBaseHelper.TABLE_MAIN, new String[]{DataBaseHelper.COLUMN_SPENT, DataBaseHelper.COLUMN_DAY, DataBaseHelper.COLUMN_SPENT_COMMENT}, where, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int spent = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_SPENT));
                int day = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUMN_DAY));
                String spentComment = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_SPENT_COMMENT));
                Log.d("mas", "!!!year = " + year + " month = " + month + " spent = " + spent + " day = " + day + " spentComment = " + spentComment);
                listOfSpentInMonth.add(new DataSpentDetailing(spent, day, month, year, spentComment));
            } while (cursor.moveToNext());
        } else {
            Log.d("mas", "Для указанного месяца нет трат");
        }
        cursor.close();
        return listOfSpentInMonth;
    }


}

class MyAdapterForDetalingExpenseList extends ArrayAdapter<String> {

    Context context;
    private String[] date;
    private int[] spent;
    private String[] spentComment;

    public MyAdapterForDetalingExpenseList(@NonNull Context context, String[] date, int[] spent, String[] spentComment) {
        super(context, R.layout.costum_item_detailing_elemnt, R.id.text1, date);
        this.context = context;
        this.date = date;
        this.spent = spent;
        this.spentComment = spentComment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.costum_item_detailing_elemnt, parent, false);
        TextView dateTextView = customView.findViewById(R.id.detailing_date);
        dateTextView.setText(date[position]);
        TextView spentTextView = customView.findViewById(R.id.detailing_spent);
        spentTextView.setText(String.valueOf(spent[position]));
        TextView spentCommentTextView = customView.findViewById(R.id.detailing_spent_comment);
        spentCommentTextView.setText(spentComment[position]);

        return customView;
    }

    public void searchDetailOfExpense(int month, int year, DataBaseHelper dataBaseHelper, SQLiteDatabase dataBase) {

    }
}
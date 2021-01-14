package com.example.myfinance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myfinance.database.DataBaseHelper;
import com.example.myfinance.fragments.FragmentAdditing;
import com.example.myfinance.fragments.FragmentDetailingOfExpense;
import com.example.myfinance.fragments.FragmentStatistics;
import com.google.android.material.textfield.TextInputEditText;
import com.roacult.backdrop.BackdropLayout;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_ITEM_MENU = 0;
    private static final int STATISTICS_ITEM_MENU = 1;
    private static final int DETAILING_OF_EXPENSE = 2;

    BackdropLayout backdropLayout;
    ListView listView;

    View front_layout, back_layout;

    private static DataBaseHelper dataBaseHelper;
    private static SQLiteDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(this);


        backdropLayout = findViewById(R.id.container);
        front_layout = backdropLayout.getChildAt(0);
        front_layout = backdropLayout.getChildAt(1);
        //устраняет возможность нажатия через поднимающуюся шторку
        front_layout.setClickable(true);

        String[] menu_item = getResources().getStringArray(R.array.menu_items);
        listView = findViewById(R.id.list_item_menu);

        MyAdapterForMenu adapterForMenu = new MyAdapterForMenu(this, menu_item);
        listView.setAdapter(adapterForMenu);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case ADD_ITEM_MENU:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, FragmentAdditing.class, null)
                                .commit();
                        backdropLayout.close();
                        break;
                    case STATISTICS_ITEM_MENU:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, FragmentStatistics.class, null)
                                .commit();
                        backdropLayout.close();
                        break;
                    case DETAILING_OF_EXPENSE:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, FragmentDetailingOfExpense.class, null)
                                .commit();
                        backdropLayout.close();
                        break;
                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    public static DataBaseHelper getDataBaseHelper() {
        return dataBaseHelper;
    }

    public static SQLiteDatabase getDataBase() {
        return dataBase;
    }
}


class MyAdapterForMenu extends ArrayAdapter<String> {

    Context context;
    private String[] title;

    public MyAdapterForMenu(@NonNull Context context, String[] title) {
        super(context, R.layout.costum_item_element, R.id.text1, title);
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View costumView = layoutInflater.inflate(R.layout.costum_item_element, parent, false);
        ImageView images = costumView.findViewById(R.id.image_item);
        TextView titleTextView = costumView.findViewById(R.id.text1);

        titleTextView.setText(title[position]);

        return costumView;
    }
}
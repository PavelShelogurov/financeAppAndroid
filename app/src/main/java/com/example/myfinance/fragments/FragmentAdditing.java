package com.example.myfinance.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfinance.MainActivity;
import com.example.myfinance.R;
import com.example.myfinance.database.DataBaseHelper;
import com.example.myfinance.dialog_fragments.ErrorDialogFragment;
import com.example.myfinance.utils.DataProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FragmentAdditing extends Fragment {

    private static final String EMPTY_STRING = "";

    Button add_button;

    //для записи даты в базу данных
    private DataProvider dataProvider;

    TextInputEditText earnedTextField, spendTextField;
    TextInputLayout textInputLayoutEarned, textInputLayoutSpent, textInputLayoutSpentComment;

    EditText editTextEarned, editTextSpent, editTextSpentComment;

    public FragmentAdditing() {
        super(R.layout.additing_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        earnedTextField = view.findViewById(R.id.text_input_earned);
        spendTextField = view.findViewById(R.id.text_input_spent);

        textInputLayoutEarned = view.findViewById(R.id.textField1);
        textInputLayoutSpent = view.findViewById(R.id.textField2);
        textInputLayoutSpentComment = view.findViewById(R.id.textField3);
        editTextEarned = textInputLayoutEarned.getEditText();
        editTextSpent = textInputLayoutSpent.getEditText();
        editTextSpentComment = textInputLayoutSpentComment.getEditText();

        dataProvider = new DataProvider();

        add_button = view.findViewById(R.id.add_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String earned = editTextEarned.getText().toString();
                String spent = editTextSpent.getText().toString();
                String spentComment = editTextSpentComment.getText().toString();
                //если пользователь ничего не ввёл в поле, то считать, что он ввёл ноль
                earned = checkOnEmptyIntString(earned);
                spent = checkOnEmptyIntString(spent);
                //так как это поле хранит текст считается что он ввёл пустую строку
                spentComment = checkOnEmptyTextString(spentComment);

                editTextEarned.setText(EMPTY_STRING);
                editTextSpent.setText(EMPTY_STRING);
                editTextSpentComment.setText(EMPTY_STRING);
                //сброс выделения с полей
                editTextEarned.clearFocus();
                editTextSpent.clearFocus();
                editTextSpentComment.clearFocus();

                //генирирование даты
                dataProvider.generate();

                try {
                    addDataToDataBase(Integer.parseInt(earned), Integer.parseInt(spent), spentComment, dataProvider.getDay(), dataProvider.getMonth(), dataProvider.getYear(), MainActivity.getDataBaseHelper(), MainActivity.getDataBase());
                    Log.d("mas", "Запись добавлена в базу данных. Заработано - " + earned + " Потрачено " + spent + " Комментарий к тратам: " + spentComment);
                    Log.d("mas", "Снегирировалась дата День " + dataProvider.getDay() + " Месяц " + dataProvider.getMonth() + " Год " + dataProvider.getYear());
                } catch (IllegalArgumentException e){
                    Log.d("mas", "ОШИБКА! Не добавлена трата, но добавлена подпить к трате!");
                    AlertDialog dialog = ErrorDialogFragment.getAlertDialogNotSpentHasSpentComment(getActivity());
                    dialog.show();
                }



            }
        });
    }

    private String checkOnEmptyIntString(String checkedString) {
        if (checkedString.equalsIgnoreCase(EMPTY_STRING)) {
            checkedString = "0";
        }
        return checkedString;
    }

    private String checkOnEmptyTextString(String checkedString) {
        if (checkedString.equals(null)) {
            checkedString = EMPTY_STRING;
        }
        return checkedString;
    }

    private void addDataToDataBase(int earned, int spent, String spentComment, int day, int month, int year, DataBaseHelper dataBaseHelper, SQLiteDatabase dataBase) {
        //если пользователь не ввёл траты а комментарий к тартам ввёл - это ошибка - выбросить исключение
        if (spent == 0 && spentComment != null) {
            throw new IllegalArgumentException("Addition Spent Comment without spent number");
        } else {
            //DataBaseHelper dataBaseHelper1 = new DataBaseHelper(getContext());
            dataBase = dataBaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues(); //объект, помогающий добавлять новые строки в таблицу

            values.put(DataBaseHelper.COLUMN_EARNED, earned);
            values.put(DataBaseHelper.COLUMN_SPENT, spent);
            values.put(DataBaseHelper.COLUMN_SPENT_COMMENT, spentComment);
            values.put(DataBaseHelper.COLUMN_DAY, day);
            values.put(DataBaseHelper.COLUMN_MONTH, month);
            values.put(DataBaseHelper.COLUMN_YEAR, year);

            dataBase.insert(DataBaseHelper.TABLE_MAIN, null, values);
        }
    }
}

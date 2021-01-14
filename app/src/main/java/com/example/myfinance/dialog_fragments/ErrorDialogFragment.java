package com.example.myfinance.dialog_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.example.myfinance.R;

public class ErrorDialogFragment extends DialogFragment {


    public static AlertDialog getAlertDialogNotSpentHasSpentComment(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        return builder.setTitle(R.string.error_title).setMessage(R.string.error_full_spent_comment).setPositiveButton("Ок", null).create();

    }

    public static AlertDialog getAlertDialogEmptyDB(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        return builder.setTitle(R.string.attention).setMessage(R.string.error_empty_data_base).setPositiveButton("Ок", null).create();

    }
}

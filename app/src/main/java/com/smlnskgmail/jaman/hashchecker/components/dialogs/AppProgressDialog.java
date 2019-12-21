package com.smlnskgmail.jaman.hashchecker.components.dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.smlnskgmail.jaman.hashchecker.tools.UITools;

public class AppProgressDialog {

    @SuppressLint("ResourceType")
    @NonNull
    public static ProgressDialog getDialog(@NonNull Context context, @IdRes int textMessageResId) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(context);
        progressDialog.setMessage(context.getString(textMessageResId));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(UITools.getCommonBackgroundColor(context))
        );
        return progressDialog;
    }

}

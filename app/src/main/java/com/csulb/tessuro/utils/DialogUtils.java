package com.csulb.tessuro.utils;

import android.app.Activity;

import com.csulb.tessuro.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    private MaterialAlertDialogBuilder dialogBuilder;

    public void successDialog(Activity activity, String message) {
        this.dialogBuilder = new MaterialAlertDialogBuilder(activity);
        this.dialogBuilder.setTitle("Success");
        this.dialogBuilder.setIcon(R.drawable.ic_success);
        this.dialogBuilder.setMessage(message);
    }

    public void errorDialog(Activity activity, String message) {
        this.dialogBuilder = new MaterialAlertDialogBuilder(activity);
        this.dialogBuilder.setTitle("Error");
        this.dialogBuilder.setIcon(R.drawable.ic_error);
        this.dialogBuilder.setMessage(message);
    }

    public void infoDialog(Activity activity, String message) {
        this.dialogBuilder = new MaterialAlertDialogBuilder(activity);
        this.dialogBuilder.setTitle("Information");
        this.dialogBuilder.setIcon(R.drawable.ic_info);
        this.dialogBuilder.setMessage(message);
    }

    public void showDialog() {
        this.dialogBuilder.show();
    }
}

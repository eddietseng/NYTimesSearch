package com.eddietseng.nytimessearch.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by eddietseng on 7/31/16.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    String date;

    // Defines the listener interface
    public interface SelectDateDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {

        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        date = year + " / " +pad(month)+ " / " +pad(day);
        sendBackResult();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        SelectDateDialogListener listener = (SelectDateDialogListener) getTargetFragment();
        listener.onFinishEditDialog(date);
        dismiss();
    }

    private String pad(int n) {
        return n<10 ? '0'+Integer.toString(n) : Integer.toString(n);
    }

}

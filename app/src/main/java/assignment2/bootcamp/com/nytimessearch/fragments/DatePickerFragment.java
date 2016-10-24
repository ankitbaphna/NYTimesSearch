package assignment2.bootcamp.com.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by baphna on 10/21/2016.
 */

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener onDateSet;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }

    public void setCallback(DatePickerDialog.OnDateSetListener callback){
        this.onDateSet = callback;
    }
}

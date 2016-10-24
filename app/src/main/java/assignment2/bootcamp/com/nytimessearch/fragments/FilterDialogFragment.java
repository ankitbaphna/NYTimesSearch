package assignment2.bootcamp.com.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import assignment2.bootcamp.com.nytimessearch.R;
import assignment2.bootcamp.com.nytimessearch.models.SelectedFilters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baphna on 10/21/2016.
 */

public class FilterDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.etDatePicker)
    TextView textDatePicker;

    @BindView(R.id.checkbox_arts)
    CheckBox checkBoxArts;

    @BindView(R.id.checkbox_fashion)
    CheckBox checkBoxFashion;

    @BindView(R.id.checkbox_sports)
    CheckBox checkBoxSports;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.spinnerSortOrder)
    Spinner spinnerSortOrder;

    public interface FilterSelectedListener {
        void onFilterSelectedListener(SelectedFilters selectedFilters);
    }

    public FilterDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Get field from view
        //textDatePicker = (TextView) view.findViewById(R.id.textDatePicker);

        /*textDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });*/

        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //textDatePicker.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
                //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCallback(FilterDialogFragment.this);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.etDatePicker)
    public void datePickerClicked(View v){
        showDatePickerDialog(v);
    }

    @OnClick(R.id.btnSave)
    public void saveClicked(){
        /*Map<String, String> filterSelection = new ArrayMap<>();
        filterSelection.put(Constants.DATE, textDatePicker.getText().toString());
        filterSelection.put(Constants.ORDER)*/
        SelectedFilters selectedFilters = new SelectedFilters(textDatePicker.getText().toString(),
                spinnerSortOrder.getSelectedItem().toString(),
                checkBoxArts.isChecked(),
                checkBoxFashion.isChecked(),
                checkBoxSports.isChecked());

        FilterSelectedListener listener = (FilterSelectedListener) getActivity();
        listener.onFilterSelectedListener(selectedFilters);
        dismiss();

    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        textDatePicker.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
    }

}

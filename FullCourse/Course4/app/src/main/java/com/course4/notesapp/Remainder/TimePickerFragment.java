package com.course4.notesapp.Remainder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerCallback {
        void getTime(int hourOfDay, int minute);
    }

    TimePickerCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (TimePickerCallback) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The parent should implement the " + TimePickerCallback.class.getSimpleName() + " callback.");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callback.getTime(hourOfDay, minute);
    }
}
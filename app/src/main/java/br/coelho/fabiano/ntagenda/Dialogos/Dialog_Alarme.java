package br.coelho.fabiano.ntagenda.Dialogos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class Dialog_Alarme extends BaseDialog {

    @BindView(R.id.dtDay) EditText day;
    @BindView(R.id.dtMonth) EditText month;
    @BindView(R.id.dtYear) EditText year;
    @BindView(R.id.dtHour) EditText hour;
    @BindView(R.id.dtMinute) EditText minute;
    @BindView(R.id.dtDone) Button done;
    @BindView(R.id.dtDayLayout) TextInputLayout dayLayout;
    @BindView(R.id.dtMonthLayout) TextInputLayout monthLayout;
    @BindView(R.id.dtYearLayout) TextInputLayout yearLayout;
    @BindView(R.id.dtHourLayout) TextInputLayout hourLayout;
    @BindView(R.id.dtMinuteLayout) TextInputLayout minuteLayout;
    @BindView(R.id.DialogTitle) TextView title;
    @BindView(R.id.dWarning) TextView warning;

    private Calendar c = Calendar.getInstance();
    private TextView alarmLong, alarmFormat;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_modelo_datetime, container, false);
        ButterKnife.bind(this, view);
        setTheme();

        title.setText(getString(R.string.dialog_setalarm_title));


        return view;
    }

    @OnTextChanged(R.id.dtDay) void rtDay(CharSequence s){
        String aux = "";
        aux += s;
        maxFocus(aux, 1, month, day);
    }

    @OnTextChanged(R.id.dtMonth) void rtMonth(CharSequence s){
        String aux = "";
        aux += s;
        maxFocus(aux, 1, year, day);
    }

    @OnTextChanged(R.id.dtYear) void rtYear(CharSequence s){
        String aux = "";
        aux += s;
        maxFocus(aux, 3, hour, month);
    }

    @OnTextChanged(R.id.dtHour) void rtHour(CharSequence s){
        String aux = "";
        aux += s;
        maxFocus(aux, 1, minute, year);
    }

    @OnTextChanged(R.id.dtMinute) void rtMinute(CharSequence s){
        String aux = "";
        aux += s;
        maxFocus(aux, 1, minute, hour);
    }

    @OnClick(R.id.dInputDialog) void onClickDialogDate(){
        DatePickerDialog dataPickerDialog = new DatePickerDialog(this.getActivity(),
                android.app.AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearOfWorld, int monthOfYear, int dayOfMonth) {
                        day.setText(Integer.toString(dayOfMonth));
                        month.setText(Integer.toString(monthOfYear+1));
                        year.setText(Integer.toString(yearOfWorld));
                        DialogTime();
                    }
                }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
        dataPickerDialog.show();

    }

    @OnClick(R.id.dtDone) void onClickDone(){
        if(!isEmpty(day, dayLayout) && !isEmpty(month, monthLayout) && !isEmpty(year, yearLayout)){
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText().toString()));
            c.set(Calendar.MONTH, Integer.parseInt(month.getText().toString())-1);
            c.set(Calendar.YEAR, Integer.parseInt(year.getText().toString()));
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.getText().toString()));
            c.set(Calendar.MINUTE, Integer.parseInt(minute.getText().toString()));

            Date myDate= c.getTime();

            warning.setVisibility(View.GONE);
            getDialog().dismiss();
            alarmLong.setText(Long.toString(c.getTimeInMillis()));
            alarmFormat.setText(formatter.format(myDate));
            getDialog().dismiss();

        }


    }

    public void setAlarm(TextView alarmLong, TextView alarmFormat){
        this.alarmLong = alarmLong;
        this.alarmFormat = alarmFormat;
    }

    private boolean isEmpty(EditText editText, TextInputLayout layout){
        if(editText.getText().toString().length() < 1){
            layout.setError(" ");
            warning.setVisibility(View.VISIBLE);
            return true;
        }else{
            return false;
        }

    }

    private void DialogTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(),
                android.app.AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        hour.setText(Integer.toString(hourOfDay));
                        minute.setText(Integer.toString(minuteOfDay));
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();

    }

    private void maxFocus(String lenght, int max, EditText nextFocus, EditText backFocus){
        if(lenght.length() > max){
            nextFocus.requestFocus();
        }else if(lenght.length() < 1){
            backFocus.requestFocus();
        }
    }






}

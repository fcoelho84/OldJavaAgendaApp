package br.coelho.fabiano.ntagenda.Auxiliares;

import android.widget.SimpleCursorAdapter;

public class Calendar_Data {

    private String Date, DayOfTheWeek;
    private SimpleCursorAdapter Lembretes;

    public void setDate(String Date){
        this.Date = Date;
    }

    public void setDayOfTheWeek(String DayOfTheWeek){
        this.DayOfTheWeek = DayOfTheWeek;
    }

    public void setLembretes(SimpleCursorAdapter Lembretes){
        this.Lembretes = Lembretes;
    }

    public String getDate() {
        return Date;
    }

    public String getDayOfTheWeek() {
        return DayOfTheWeek;
    }

    public SimpleCursorAdapter getLembretes() {
        return Lembretes;
    }
}

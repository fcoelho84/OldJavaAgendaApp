package br.coelho.fabiano.ntagenda.Auxiliares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import br.coelho.fabiano.ntagenda.R;

public class Calendar_FormatView {


    private String[] from = {"nome", "datastr", "tipo", "idalarme", "grau", "iddesc", "_id"};
    private int[] to = {R.id.mTitle_Id, R.id.mData_Id, R.id.mTag_Id,
            R.id.ID_ALARME, R.id.ID_COLOR, R.id.ID_DESC, R.id.ID_ID};

    private  Aux_SetHeightList setHeightList =  new Aux_SetHeightList();

    private Context context;
    private ListView container;
    private SQLiteDatabase db;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


    public void setContext(Context context){
        this.context = context;
    }

    public void setListView(ListView container){
        this.container = container;
    }

    public void setListItems(ArrayList<String> Dates){
        ArrayList<Calendar_Data> items = new ArrayList<>();

        int i = 0;

        while(i < Dates.size()){

            Calendar_Data calendarItem = new Calendar_Data();

            calendarItem.setDate(getDateTitle(Dates.get(i)));
            calendarItem.setDayOfTheWeek(getDayOfTheWeek(Dates.get(i)));
            calendarItem.setLembretes(getLembretes(Dates.get(i)));

            items.add(calendarItem);

            ++i;
        }

        setListAdapter(items);
    }

    public void setCalendarEvent(ArrayList<String> Dates, CompactCalendarView lCalendar){
        try{
            lCalendar.removeAllEvents();

            int colorId = context.getResources().getColor(R.color.colorPrimary);

            for(int i=0;i<Dates.size();i++){

                Date dateEvent = formatter.parse(Dates.get(i));

                Event event = new Event(colorId, dateEvent.getTime());

                lCalendar.addEvent(event);
                lCalendar.shouldDrawIndicatorsBelowSelectedDays(true);
            }

        }catch (Exception e){
            Log.e("CALENDAR_FORMAT", "setCalendarEvent() Error = "+e.toString());

        }


    }

    public void setOnDayClick(Date dateClicked){
        ArrayList<Calendar_Data> items = new ArrayList<>();

        Calendar_Data calendarItem = new Calendar_Data();
        String aux = formatter.format(dateClicked);

        calendarItem.setDate(getDateTitle(aux));
        calendarItem.setDayOfTheWeek(getDayOfTheWeek(aux));
        calendarItem.setLembretes(getLembretes(aux));

        items.add(calendarItem);

        setListAdapter(items);

    }

    private String getDateTitle(String date){
        String[] array = date.substring(0,10).split("/");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(array[0]));
        c.set(Calendar.MONTH, Integer.parseInt(array[1])-1);
        c.set(Calendar.YEAR, Integer.parseInt(array[2]));

        Date myDate = c.getTime();

        SimpleDateFormat f =  new SimpleDateFormat("dd  MMM.  yyyy");

        return f.format(myDate);
    }

    private String getDayOfTheWeek(String date){
        String[] array = date.substring(0,10).split("/");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(array[0]));

        Date myDate = c.getTime();

        SimpleDateFormat f = new SimpleDateFormat("EEEE");

        return f.format(myDate);

    }

    private SimpleCursorAdapter getLembretes(String date){
        SimpleCursorAdapter ad = null;
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+""+
                    " WHERE  datastr LIKE '%"+date+"%' ORDER BY data ASC", null);

            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_lembrete, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    String color = ((TextView) row.findViewById(R.id.ID_COLOR)).getText().toString(),
                            alarme = ((TextView) row.findViewById(R.id.ID_ALARME)).getText().toString(),
                            tag = ((TextView) row.findViewById(R.id.mTag_Id)).getText().toString();


                    (row.findViewById(R.id.mCor_Id)).setBackgroundColor(Integer.parseInt(color));
                    String date = ((TextView) row.findViewById(R.id.mData_Id)).getText().toString();
                    ((TextView) row.findViewById(R.id.SAVE_DATE)).setText(date);
                    ((TextView) row.findViewById(R.id.mData_Id)).setText(date.substring(11));

                    if(!(tag.length() > 0)){
                        (row.findViewById(R.id.mBarra_Id)).setVisibility(View.GONE);
                    }

                    if(alarme.equals("") || alarme.equals(BD_Database.NOTIFICACAO)){
                        (row.findViewById(R.id.mAlarme_Id)).setVisibility(View.GONE);
                    }

                    return row;
                }
            };
        }catch (Exception e){
            Log.e("Calendar_Format","ERRO = "+e);
        }

        return ad;
    }

    private void setListAdapter(ArrayList<Calendar_Data> items){
        container.setAdapter(new Calendar_AdapterList(context, items));
        container.setDivider(null);

        setHeightList.setListViewHeightBasedOnChildren(container);

    }


}

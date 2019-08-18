package br.coelho.fabiano.ntagenda.Tags;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.coelho.fabiano.ntagenda.Auxiliares.Calendar_FormatView;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Frag_tag_Basecalendar extends Fragment {


    private View view;

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();

    private Calendar_FormatView CalendarFormat = new Calendar_FormatView();

    private SimpleDateFormat format;

    @BindView(R.id.lCalendar) CompactCalendarView myCalendar;

    @BindView(R.id.ini_calendar_title) TextView cTitle;

    @BindView(R.id.lContainer) ListView myList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ButterKnife.bind(this, getMyTagView());

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(this.getActivity().getFragmentManager());

        format = new SimpleDateFormat("MMMM, yyyy");

        CalendarFormat.setContext(this.getActivity());
        CalendarFormat.setListView(myList);

        cTitle.setText(format.format(Calendar.getInstance().getTime()));

        myCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                CalendarFormat.setOnDayClick(dateClicked);
            }

            @Override
            public void onMonthScroll(Date date) {
                cTitle.setText(format.format(date));
            }
        });



        return getMyTagView();
    }

    private View getMyTagView(){

        return view;
    }

    public void setMyTagView(View view){
        this.view = view;

    }

}

package br.coelho.fabiano.ntagenda.Inicio;

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

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Calendar_FormatView;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Frag_ini_LembretesCalendar extends Fragment {

    @BindView(R.id.lCalendar) CompactCalendarView lCalendar;
    @BindView(R.id.lContainer) ListView lContainer;
    @BindView(R.id.ini_calendar_title) TextView cTitle;

    private Calendar_FormatView CalendarFormat = new Calendar_FormatView();
    private SimpleDateFormat format;
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private BD_Query bdQuery = new BD_Query();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ini_lembrete_calendario, container, false);
        ButterKnife.bind(this, view);

        format = new SimpleDateFormat("MMMM, yyyy");

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(getActivity().getFragmentManager());

        CalendarFormat.setContext(this.getActivity());
        CalendarFormat.setListView(lContainer);

        bdQuery.setContext(this.getActivity());

        lCalendar.setUseThreeLetterAbbreviation(true);

        setCalendarContent();

        lCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                CalendarFormat.setOnDayClick(dateClicked);
            }

            @Override
            public void onMonthScroll(Date date) {
               cTitle.setText(format.format(date));
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        setCalendarContent();

        super.onStart();

    }

    private void setCalendarContent(){
        CalendarFormat.setListItems(bdQuery.PegarAsDatasDosLembretes(false));
        CalendarFormat.setCalendarEvent(bdQuery.PegarAsDatasDosLembretes(true), lCalendar);
        cTitle.setText(format.format(Calendar.getInstance().getTime()));
    }

}

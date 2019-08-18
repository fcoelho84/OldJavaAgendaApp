package br.coelho.fabiano.ntagenda.Auxiliares;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Calendar_AdapterList extends BaseAdapter{

    @BindView(R.id.TitleCalendarList) TextView DateTitle;
    @BindView(R.id.cTitleWeek) TextView DayOfTheWeekTitlte;
    @BindView(R.id.ListCalendarItems) ListView list;

    @OnItemLongClick(R.id.ListCalendarItems) boolean onLongClickItem(View view){
        comunicacao.openModeEditLembrete(new Intent(context, ActivityLembrete.class), view);
        return true;
    }

    @OnItemClick(R.id.ListCalendarItems) void onItemClick(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));
    }


    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private Context context;
    private ArrayList<Calendar_Data> items;

    public Calendar_AdapterList(Context context, ArrayList<Calendar_Data> items){
        this.context = context;
        this.items = items;
        comunicacao.setContext(context);
        comunicacao.setFM(((Activity) context).getFragmentManager());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Calendar_Data calendarItem = items.get(position);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view = inflater.inflate(R.layout.layout_modelo_calendar_item, null);

        ButterKnife.bind(this, view);

        DateTitle.setText(calendarItem.getDate());
        DayOfTheWeekTitlte.setText(calendarItem.getDayOfTheWeek());

        list.setAdapter(calendarItem.getLembretes());
        list.setDivider(null);

        Aux_SetHeightList listSize = new Aux_SetHeightList();
        listSize.setListViewHeightBasedOnChildren(list);

        return view;
    }

}

package br.coelho.fabiano.ntagenda.Auxiliares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.coelho.fabiano.ntagenda.R;

public class BD_Query {

    private Context context;
    private String[] from = {"nome", "datastr", "tipo", "idalarme", "grau", "iddesc", "_id", "timealarme"};
    private int[] to = {R.id.mTitle_Id, R.id.mData_Id, R.id.mTag_Id,
            R.id.ID_ALARME, R.id.ID_COLOR, R.id.ID_DESC, R.id.ID_ID, R.id.TIME_ALARM};

    private SQLiteDatabase db = null;
    private SimpleCursorAdapter ad = null;
    private Cursor cursor = null;

    public void setContext(Context context){
        this.context = context;
    }

    private long getCurrentDate(){

        return System.currentTimeMillis();
    }


    // Adapters

    public SimpleCursorAdapter PegarOsLembretesPendentes(){
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from "+ BD_Database.TABLE_LEMBRETES+" " +
                    "WHERE data >= "+getCurrentDate()+" ORDER BY data ASC", null);
            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_lembrete, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    View row = super.getView(position, convertView, parent);

                    String color = ((TextView) row.findViewById(R.id.ID_COLOR)).getText().toString(),
                            alarme = ((TextView) row.findViewById(R.id.ID_ALARME)).getText().toString(),
                            tag = ((TextView) row.findViewById(R.id.mTag_Id)).getText().toString();
                    (row.findViewById(R.id.mCor_Id)).setBackgroundColor(Integer.parseInt(color));

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
            Log.e("BD_QUERY", "QUERY_pLembrete = "+e);
        }
        return ad;
    }

    public SimpleCursorAdapter PegarOsLembretesPorTag(String tag, boolean pendente){
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);

            if(pendente){
                cursor = db.rawQuery("SELECT * from "+ BD_Database.TABLE_LEMBRETES+" " +
                        "WHERE data >= "+getCurrentDate()+" AND tipo = '"+tag+"' ORDER BY data ASC", null);
            }else{
                cursor = db.rawQuery("SELECT * from "+ BD_Database.TABLE_LEMBRETES+" " +
                        "WHERE tipo = '"+tag+"' ORDER BY data ASC", null);
            }

            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_lembrete, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    View row = super.getView(position, convertView, parent);

                    String color = ((TextView) row.findViewById(R.id.ID_COLOR)).getText().toString(),
                            alarme = ((TextView) row.findViewById(R.id.ID_ALARME)).getText().toString(),
                            tag = ((TextView) row.findViewById(R.id.mTag_Id)).getText().toString();
                    (row.findViewById(R.id.mCor_Id)).setBackgroundColor(Integer.parseInt(color));

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
            Log.e("BD_QUERY", "QUERY_pLembrete = "+e);
        }
        return ad;
    }

    public SimpleCursorAdapter PegarOsLembretes(CharSequence newText, boolean search){

        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            if(search){
                cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" WHERE nome LIKE '%" + newText + "%' OR tipo LIKE '%" + newText + "%' OR datastr LIKE '%" + newText + "%'", null);
            }else{
                cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" ORDER BY data ASC", null);
            }

            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_lembrete, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    View row = super.getView(position, convertView, parent);

                    String color = ((TextView) row.findViewById(R.id.ID_COLOR)).getText().toString(),
                            alarme = ((TextView) row.findViewById(R.id.ID_ALARME)).getText().toString(),
                            tag = ((TextView) row.findViewById(R.id.mTag_Id)).getText().toString();
                    (row.findViewById(R.id.mCor_Id)).setBackgroundColor(Integer.parseInt(color));

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
            Log.e("BD_QUERY", "QUERY_pLembrete = "+e);
        }

        return ad;

    }

    public SimpleCursorAdapter PegarOsLembretesComNotificacao(){
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from "+ BD_Database.TABLE_LEMBRETES+" " +
                    "WHERE idalarme = ? ORDER BY data ASC", new String[]{""+BD_Database.NOTIFICACAO});
            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_lembrete, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    View row = super.getView(position, convertView, parent);

                    String color = ((TextView) row.findViewById(R.id.ID_COLOR)).getText().toString(),
                            alarme = ((TextView) row.findViewById(R.id.ID_ALARME)).getText().toString(),
                            tag = ((TextView) row.findViewById(R.id.mTag_Id)).getText().toString();
                    (row.findViewById(R.id.mCor_Id)).setBackgroundColor(Integer.parseInt(color));

                    if(!(tag.length() > 0)){
                        (row.findViewById(R.id.mBarra_Id)).setVisibility(View.GONE);
                    }

                    if(alarme.equals("")){
                        (row.findViewById(R.id.mAlarme_Id)).setVisibility(View.GONE);
                    }
                    return row;
                }
            };
        }catch (Exception e){
            Log.e("BD_QUERY", "QUERY_pLembrete = "+e);
        }

        return ad;
    }

    public SimpleCursorAdapter PegarAsAnotacoes(CharSequence newText, boolean search){
        try{
            String[] from = {"data", "deesc", "grau", "_id"};
            int[] to = {R.id.Data, R.id.Desc, R.id.Grau, R.id.Id};
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
           if(search){
               cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ANOTACOES+" WHERE data LIKE '%" + newText + "%' OR deesc LIKE '%" + newText + "%'", null);
           }else{
               cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ANOTACOES+" ORDER BY data ASC", null);
           }

            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_anotacao, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    String color =  ((TextView) row.findViewById(R.id.Grau)).getText().toString();
                    (row.findViewById(R.id.Notes)).setBackgroundColor(Integer.parseInt(color));
                    return row;
                }
            };
        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_Anotacoes = "+e);
        }

        return ad;
    }

    public SimpleCursorAdapter PegarListaDeResumosDeAnotacoes(){
        try{
            String[] from = {"deesc", "grau", "_id"};
            int[] to = {R.id.Desc, R.id.Grau, R.id.Id};
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);

            cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ANOTACOES+" ORDER BY data ASC", null);

            ad = new SimpleCursorAdapter(context, R.layout.layout_modelo_item_anotacao_breve, cursor, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    TextView myNote = row.findViewById(R.id.Desc);
                    String note = myNote.getText().toString();
                    (row.findViewById(R.id.Notes))
                            .setBackgroundColor(
                                    Integer.parseInt(
                                            ((TextView)
                                                    row.findViewById(R.id.Grau)).getText().toString()));
                    myNote.setText(strSub(note));
                    return row;
                }
            };


        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_ANEX_Anotacoes = "+e);
        }

        return ad;
    }

    // Listas

    public ArrayList<String> PegarAsAnotacoesPorAnexo(int ID){
        ArrayList<String> aux = new ArrayList<>();

        try{
            SQLiteDatabase db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ANOTACOES+" WHERE _id = ?", new String[]{""+ID});

            if(cursor.moveToFirst()){
                aux.add(cursor.getString(cursor.getColumnIndex("deesc")));
                aux.add(strSub(cursor.getString(cursor.getColumnIndex("deesc"))));

            }

        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_ANEXBYID_Anotacoes = "+e);
        }

        return aux;
    }

    public ArrayList<String> PegarAsDatasDosLembretes(boolean duplicate){
        ArrayList<String> myList = new ArrayList<>();
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" ORDER BY data ASC", null);

            while(cursor.moveToNext()){
                String aux = cursor.getString(cursor.getColumnIndex("datastr")).substring(0,10);
                if(duplicate){
                    myList.add(aux);
                }else{
                    if(!myList.contains(aux)){
                        myList.add(aux);
                    }
                }

            }

        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_DATE_Lemprete = "+e);

        }
        return myList;
    }

    public ArrayList<String> PegarOsIdsDasDatasDosLembretesAntigos(){
        ArrayList<String> myList = new ArrayList<>();
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" WHERE  data <= "+getCurrentDate(),null);

            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("_id")),
                        data = cursor.getString(cursor.getColumnIndex("data"));
               myList.add(id +";"+ data);

            }

        }catch(Exception e){
            Log.e("BD_QUERY","PegarOsIdsDasDatasDosLembretesAntigos() = "+e);

        }
        return myList;
    }

    public ArrayList<Integer> PegarOsIdsDasAnotacoes(){
        ArrayList<Integer> myList = new ArrayList<>();
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ANOTACOES, null);

            while(cursor.moveToNext()){
                String aux = cursor.getString(cursor.getColumnIndex("_id"));
                myList.add(Integer.parseInt(aux));

            }

        }catch(Exception e){
            Log.e("BD_QUERY","PegarOsIdsDasAnotacoes()s = "+e);

        }
        return myList;
    }

    public ArrayList<String> PegarAsDatasDosLembretesPorTag(String tag, boolean pendente, boolean duplicate){
        ArrayList<String> myList = new ArrayList<>();
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            if(pendente){
                cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" WHERE data >= "+getCurrentDate()+" AND tipo = '"+tag+"' ORDER BY data ASC", null);
            }else{
                cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_LEMBRETES+" WHERE tipo = '"+tag+"' ORDER BY data ASC", null);
            }

            while(cursor.moveToNext()){
                String aux = cursor.getString(cursor.getColumnIndex("datastr")).substring(0,10);
                if(duplicate){
                    myList.add(aux);
                }else{
                    if(!myList.contains(aux)){
                        myList.add(aux);
                    }
                }

            }

        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_DATE_Lemprete = "+e);

        }
        return myList;
    }

    public ArrayAdapter PegarTodasAsTagsDosLembretes() {
        ArrayAdapter<String> myTags = null;
        try {
            List<String> myList = new ArrayList<>();
            SQLiteDatabase db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT tipo FROM " +BD_Database.TABLE_LEMBRETES+ " ORDER BY tipo ASC", null);

            while (cursor.moveToNext()) {
                String aux = cursor.getString(cursor.getColumnIndex("tipo"));
                if (!aux.equals("")) {
                    myList.add(aux);
                }
            }


            ArrayList<String> Duplicate = new ArrayList<>(myList);
            LinkedHashSet<String> Hash = new LinkedHashSet<>();
            Hash.addAll(Duplicate);
            myList.clear();
            myList.addAll(Hash);
            myTags = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, myList);

        } catch (Exception e) {
            Log.e("BD_QUERY", "QUERY_TAG_Lembrete = " + e);

        }
        return myTags;
    }

    public ArrayList<String> PegarOsDadosDoAlarme(){
        ArrayList<String> myAlarms = new ArrayList<>();
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_ALARMES, null);

            if(cursor.moveToFirst()){
                while(cursor.moveToNext()){
                    String nome = cursor.getString(cursor.getColumnIndex("nome")),
                           desc = cursor.getString(cursor.getColumnIndex("strdesc")),
                           time = cursor.getString(cursor.getColumnIndex("alarme")),
                           id = cursor.getString(cursor.getColumnIndex("_id"));

                    myAlarms.add(nome+";"+desc+";"+time+";"+id);
                }

            }

        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_ALARM_DATA = "+e);
        }

        return myAlarms;
    }

    public ArrayList<String> PegarMinhasTags(){
        ArrayList<String> tags = new ArrayList<>();
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * from "+BD_Database.TABLE_TAGS, null);

            if(cursor.moveToFirst()){
                while(cursor.moveToNext()){

                    String id = cursor.getString(cursor.getColumnIndex("_id")),
                            tag = cursor.getString(cursor.getColumnIndex("nome"));

                    tags.add(id+";"+tag);
                }
            }

        }catch(Exception e){
            Log.e("BD_QUERY","PegarMinhasTags() erro = "+e);
        }

        return tags;
    }

    // Strings

    public String PegarIdDoAlarmePorData(long time){
        String id = "";
        try{
            db = context.openOrCreateDatabase(BD_Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT _id from "+BD_Database.TABLE_ALARMES+" WHERE CAST(alarme as integer) = ?",new String[]{""+time});

            while(cursor.moveToNext()){
                id = cursor.getString(cursor.getColumnIndex("_id"));
            }


        }catch(Exception e){
            Log.e("BD_QUERY","QUERY_ANEX_Anotacoes = "+e);
        }

        return id;
    }

    private String strSub(String str){
        if(str.length() > 24){
           return  str.substring(0, 24)+"...";
        }else{
            return  str+"...";
        }
    }







}

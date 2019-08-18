package br.coelho.fabiano.ntagenda.Auxiliares;


import android.content.Context;
import android.content.SharedPreferences;

public class Preferences_IO {

    public static final String SAVE_MYTAG = "0";

    // Necessario {Número da tag} + Prefixo

    public static final String SAVE_PREF_MYTAG_CALENDAR_VIEW = "CALENDAR";

    public static final String SAVE_PREF_MYTAG_DB_QUERY_PENDING = "QUERY_PENDING";

    public static final String SAVE_PREF_MYTAG_TAB_CREATE = "TAB_CREATE_SIZE";

    public static final String SAVE_CONGIF_LIST = "1";

    public static final String SAVE_CONGIF_CALENDAR = "2";

    public static final String SAVE_CONGIF_NOTE = "3";

    public static final String LANGUAGE_APP = "4";

    public void salvarPreferencias(Context c, String id, String info){
        SharedPreferences sp =
                c.getSharedPreferences(id, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(id, info);
        editor.apply();


    }

    public String pegarPreferencias(Context c, String id){
        SharedPreferences sp =
                c.getSharedPreferences(id, Context.MODE_PRIVATE);

        return sp.getString(id, "");

    }




}

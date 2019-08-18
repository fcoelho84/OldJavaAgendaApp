package br.coelho.fabiano.ntagenda.Auxiliares;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class BD_Database extends SQLiteOpenHelper{
    public static String DATABASE_NAME = "NTBD";
    private static final int DATABASE_VERSION = 16;

    public static final String TABLE_LEMBRETES = "NTlembretes";
    public static final String TABLE_ANOTACOES = "NTanotacoes";
    public static final String TABLE_ALARMES = "NTalarmes";
    public static final String TABLE_TAGS = "NTtags";

    public static final String NOTIFICACAO = "NOTIFICACAO";

    private static final String CREATE_TABLE_LEMBRETES = "create table "+TABLE_LEMBRETES+" ("+
            "_id integer primary key autoincrement,"+
            "nome text,"+
            "data integer,"+
            "tipo text,"+
            "idalarme text,"+
            "timealarme integer,"+
            "grau integer,"+
            "iddesc integer,"+
            "datastr text)";


    private static final String CREATE_TABLE_ALARMES = "create table "+TABLE_ALARMES+" ("+
            "_id integer primary key autoincrement,"+
            "nome text,"+
            "alarme integer,"+
            "strdesc text)";


    private static final String CREATE_TABLE_ANOTACOES = "create table "+TABLE_ANOTACOES+" ("+
            "_id integer primary key autoincrement,"+
            "data text,"+
            "grau integer,"+
            "deesc text)";

    private static final String CREATE_TABLE_TAGS = "create table "+TABLE_TAGS+" ("+
            "_id integer primary key autoincrement,"+
            "nome text)";



    public BD_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TABLE_LEMBRETES);
            db.execSQL(CREATE_TABLE_ANOTACOES);
            db.execSQL(CREATE_TABLE_ALARMES);
            db.execSQL(CREATE_TABLE_TAGS);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            DELETE_TABLE(TABLE_LEMBRETES);
            DELETE_TABLE(TABLE_ANOTACOES);
            DELETE_TABLE(TABLE_ALARMES);
            DELETE_TABLE(TABLE_TAGS);

        }
        onCreate(db);
    }


    public void DELETE_TABLE(String TABLE_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

    }

    // TAGS

    public void INSERT_TAGS(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);;
        db.insert(TABLE_TAGS, null, values);
    }

    public void UPDATE_TAGS(String nome, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);;
        db.update(TABLE_TAGS, values, "_id = ?", new String[]{""+id});
    }

    public void DROP_TAGS(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGS,"_id = ?", new String[]{""+id});
    }

    // ALARMES

    public void INSERT_ALARM(String nome, long alarme, String strdesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("alarme", alarme);
        values.put("strdesc", strdesc);
        db.insert(TABLE_ALARMES, null, values);
    }

    public void UPDATE_ALARM(String nome, long alarme, String strdesc, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("alarme", alarme);
        values.put("strdesc", strdesc);
        db.update(TABLE_ALARMES, values, "_id = ?", new String[]{""+id});
    }

    public void DROP_ALARM(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMES, "_id = ?", new String[]{""+id});
    }

    // tAREFAS

    public void INSERT_TASK(String nome, long data, String idalarme, long timealarme, String tipo,
                            int grau, int iddesc, String datastr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("data", data);
        values.put("idalarme", idalarme);
        values.put("timealarme", timealarme);
        values.put("tipo", tipo);
        values.put("grau", grau);
        values.put("iddesc", iddesc);
        values.put("datastr", datastr);
        db.insert(TABLE_LEMBRETES, null, values);
    }

    public void UPDATE_TASK_BY_ALARM(String idalarme, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idalarme", value);
        db.update(TABLE_LEMBRETES, values, "idalarme = ?", new String[]{""+idalarme});
    }

    public void UPDATE_TASK(String nome, long data, String idalarme, long timealarme, String tipo,
                            int grau, int iddesc,String datastr, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("data", data);
        values.put("idalarme", idalarme);
        values.put("timealarme", timealarme);
        values.put("tipo", tipo);
        values.put("grau", grau);
        values.put("iddesc", iddesc);
        values.put("datastr", datastr);
        db.update(TABLE_LEMBRETES, values, "_id = ?", new String[]{""+id});
    }

    public void DROP_TASK(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LEMBRETES, "_id = ?", new String[]{""+id});
    }

    // ANOTAÇÕES

    public void INSERT_NOTE(String data, int grau, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data", data);
        values.put("grau", grau);
        values.put("deesc", desc);
        db.insert(TABLE_ANOTACOES, null, values);
    }

    public void UPDATE_NOTE(String data, int grau, String desc, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data", data);
        values.put("grau", grau);
        values.put("deesc", desc);
        db.update(TABLE_ANOTACOES, values, "_id = ?", new String[]{""+id});
    }

    public void DROP_NOTE(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANOTACOES, "_id = ?", new String[]{""+id});
    }

    public void REMOVE_NOTE_ID_LEMBRETES(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("iddesc", -1);
        db.update(TABLE_LEMBRETES, values, "iddesc = ?", new String[]{""+id});
    }


}

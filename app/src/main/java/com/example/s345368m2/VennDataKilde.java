package com.example.s345368m2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class VennDataKilde {
    private SQLiteDatabase database;
    private DatabaseHjelper dbHelper;

    public VennDataKilde(Context context) {
        dbHelper = new DatabaseHjelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Venn leggInnVenn(String navn, String telefon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_VENN_NAVN, navn);
        values.put(DatabaseHjelper.KOLONNE_TELEFON, telefon);
        long insertId = database.insert(DatabaseHjelper.TABELL_VENNER, null, values);
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null, DatabaseHjelper.KOLONNE_VENN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Venn nyVenn = cursorTilVenn(cursor);
        cursor.close();
        return nyVenn;
    }

    private Venn cursorTilVenn(Cursor cursor) {
        Venn venn = new Venn();

        venn.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_VENN_ID)));
        venn.setNavn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_VENN_NAVN)));
        venn.setTelefon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_TELEFON)));
        return venn;
    }

    public List<Venn> finnAlleVenner() {
        List<Venn> venner = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null, null, null, null, null, DatabaseHjelper.KOLONNE_VENN_NAVN + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Venn venn = cursorTilVenn(cursor);
            venner.add(venn);
            cursor.moveToNext();
        }
        cursor.close();
        return venner;
    }

    public Venn finnVennMedId(long vennId) {
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null, DatabaseHjelper.KOLONNE_VENN_ID + " = " + vennId, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Venn venn = cursorTilVenn(cursor);
            cursor.close();
            return venn;
        }
        return null;
    }

    public void oppdaterVenn(long id, String navn, String telefon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_VENN_NAVN, navn);
        values.put(DatabaseHjelper.KOLONNE_TELEFON, telefon);
        database.update(
                DatabaseHjelper.TABELL_VENNER,
                values,
                DatabaseHjelper.KOLONNE_VENN_ID + " =? ",
                new String[]{Long.toString(id)}
        );
    }

    public void slettVenn(long vennId) {
        database.delete(DatabaseHjelper.TABELL_AVTALER, DatabaseHjelper.KOLONNE_VENN_ID + " = ?", new String[]{Long.toString(vennId)});

        database.delete(DatabaseHjelper.TABELL_VENNER, DatabaseHjelper.KOLONNE_VENN_ID + " =? ", new String[]{Long.toString(vennId)});
    }
}

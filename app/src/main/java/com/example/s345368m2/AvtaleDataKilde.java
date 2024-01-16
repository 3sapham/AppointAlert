package com.example.s345368m2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AvtaleDataKilde {
    private SQLiteDatabase database;
    private DatabaseHjelper dbHelper;

    private VennDataKilde vennDataKilde;

    public AvtaleDataKilde(Context context) {
        dbHelper = new DatabaseHjelper(context);
        vennDataKilde = new VennDataKilde(context);
        vennDataKilde.open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Avtale leggInnAvtale(String tittel, String dato, String klokkeslett, String treffsted, long vennId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_TITTEL, tittel);
        values.put(DatabaseHjelper.KOLONNE_DATO, dato);
        values.put(DatabaseHjelper.KOLONNE_KLOKKESLETT, klokkeslett);
        values.put(DatabaseHjelper.KOLONNE_TREFFSTED, treffsted);
        values.put(DatabaseHjelper.KOLONNE_VENN_ID, vennId);
        long insertId = database.insert(DatabaseHjelper.TABELL_AVTALER, null, values);
        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null, DatabaseHjelper.KOLONNE_AVTALE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Avtale nyAvtale = cursorTilAvtale(cursor);
        cursor.close();
        return nyAvtale;
    }

    private Avtale cursorTilAvtale(Cursor cursor) {
        Avtale avtale = new Avtale();
        avtale.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_ID)));
        avtale.setTittel(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_TITTEL)));
        avtale.setDato(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_DATO)));
        avtale.setKlokkeslett(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_KLOKKESLETT)));
        avtale.setTreffsted(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_TREFFSTED)));
        long vennId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_VENN_ID));
        Venn venn = vennDataKilde.finnVennMedId(vennId);
        avtale.setVenn(venn);

        return avtale;
    }

    public List<Avtale> finnAlleAvtaler() {
        List<Avtale> avtaler = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Avtale avtale = cursorTilAvtale(cursor);
            avtaler.add(avtale);
            cursor.moveToNext();
        }
        cursor.close();
        Collections.sort(avtaler, new Comparator<Avtale>() {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            @Override
            public int compare(Avtale avtale1, Avtale avtale2) {
                try {
                    Date dato1 = format.parse(avtale1.getDato());
                    Date dato2 = format.parse(avtale2.getDato());
                    return dato1.compareTo(dato2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return avtaler;
    }

    public Avtale finnAvtaleMedId(long avtaleId) {
        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null, DatabaseHjelper.KOLONNE_AVTALE_ID + " = " + avtaleId, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Avtale avtale = cursorTilAvtale(cursor);
            cursor.close();
            return avtale;
        }
        return null;
    }

    public void oppdaterAvtale(long id, String tittel, String dato, String klokkeslett, String treffsted, long vennId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_TITTEL, tittel);
        values.put(DatabaseHjelper.KOLONNE_DATO, dato);
        values.put(DatabaseHjelper.KOLONNE_KLOKKESLETT, klokkeslett);
        values.put(DatabaseHjelper.KOLONNE_TREFFSTED, treffsted);
        values.put(DatabaseHjelper.KOLONNE_VENN_ID, vennId);
        database.update(
                DatabaseHjelper.TABELL_AVTALER,
                values,
                DatabaseHjelper.KOLONNE_AVTALE_ID + " =? ",
                new String[]{Long.toString(id)}
        );
    }

    public void slettAvtale(long avtaleId) {
        database.delete(DatabaseHjelper.TABELL_AVTALER, DatabaseHjelper.KOLONNE_AVTALE_ID + " =? ", new String[]{Long.toString(avtaleId)});
    }

    public List<Avtale> finnAvtalerForIdag() {
        List<Avtale> avtaler = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String datoIdag = String.format(Locale.getDefault(),"%02d/%02d/%d", day, month, year);

        Cursor cursor = database.query(
                DatabaseHjelper.TABELL_AVTALER,
                null,
                "Dato = ?",
                new String[]{datoIdag},
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Avtale avtale = cursorTilAvtale(cursor);
                avtaler.add(avtale);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return avtaler;
    }
}

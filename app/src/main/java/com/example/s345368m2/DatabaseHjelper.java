package com.example.s345368m2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "avtaler.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABELL_AVTALER = "avtaler";
    public static final String TABELL_VENNER = "venner";

    public static final String KOLONNE_VENN_ID = "vennId";
    public static final String KOLONNE_VENN_NAVN = "navn";
    public static final String KOLONNE_TELEFON= "telefon";

    public static final String KOLONNE_AVTALE_ID = "avtaleId";
    public static final String KOLONNE_TITTEL = "navn";
    public static final String KOLONNE_DATO = "dato";
    public static final String KOLONNE_KLOKKESLETT = "klokkeslett";
    public static final String KOLONNE_TREFFSTED = "treffsted";

    private static final String TABELL_VENNER_CREATE =
            "CREATE TABLE " + TABELL_VENNER + " (" +
                    KOLONNE_VENN_ID + " INTEGER PRIMARY KEY, " +
                    KOLONNE_VENN_NAVN + " TEXT NOT NULL, " +
                    KOLONNE_TELEFON + " TEXT NOT NULL " +
                    ");";

    private static final String TABELL_AVTALER_CREATE =
            "CREATE TABLE " + TABELL_AVTALER + " (" +
                    KOLONNE_AVTALE_ID + " INTEGER PRIMARY KEY, " +
                    KOLONNE_VENN_ID + " INTEGER, " +
                    KOLONNE_TITTEL + " TEXT NOT NULL, " +
                    KOLONNE_DATO + " TEXT NOT NULL, " +
                    KOLONNE_KLOKKESLETT + " TEXT NOT NULL, " +
                    KOLONNE_TREFFSTED + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + KOLONNE_VENN_ID + ") REFERENCES " + TABELL_VENNER + "(" + KOLONNE_VENN_ID + ") ON DELETE SET NULL" +
                    ");";

    public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABELL_VENNER_CREATE);
        db.execSQL(TABELL_AVTALER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

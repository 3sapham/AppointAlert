package com.example.s345368m2;

import java.sql.Time;
import java.util.Date;

public class Avtale {
    private long id;
    private String tittel;
    private String dato;
    private String klokkeslett;
    private String treffsted;
    private Venn venn;

    public Avtale() {}

    public Avtale(long id, String tittel, String dato, String klokkeslett, String treffsted, Venn venn) {
        this.id = id;
        this.tittel = tittel;
        this.dato = dato;
        this.klokkeslett = klokkeslett;
        this.treffsted = treffsted;
        this.venn = venn;
    }

    @Override
    public String toString() {
        return tittel + " med " + getVenn().getNavn() + "\n" + klokkeslett + " " + dato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getKlokkeslett() {
        return klokkeslett;
    }

    public void setKlokkeslett(String klokkeslett) {
        this.klokkeslett = klokkeslett;
    }

    public String getTreffsted() {
        return treffsted;
    }

    public void setTreffsted(String treffsted) {
        this.treffsted = treffsted;
    }

    public Venn getVenn() {
        return venn;
    }

    public void setVenn(Venn venn) {
        this.venn = venn;
    }
}

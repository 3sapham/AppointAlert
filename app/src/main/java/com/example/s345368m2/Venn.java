package com.example.s345368m2;

public class Venn {
    private long id;
    private String navn;
    private String telefon;

    public Venn() {}

    public Venn(long id, String navn, String telefon) {
        this.id = id;
        this.navn = navn;
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return navn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}

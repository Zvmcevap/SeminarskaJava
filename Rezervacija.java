import java.io.*;
import java.util.*;


public class Rezervacija {

    private final String uporabniskoImeUporabnika;
    private final String imeUporabnika;
    private final String priimekUporabnika;

    private int steviloOdraslih;
    private int steviloOtrok;

    private final int idTermina;

    public Rezervacija( int idTermina, String uporabniskoIme, String ime, String priimek, int odrasli, int otroci) {

        this.idTermina = idTermina;
        this.uporabniskoImeUporabnika = uporabniskoIme;
        this.imeUporabnika = ime;
        this.priimekUporabnika = priimek;
        this.steviloOdraslih = odrasli;
        this.steviloOtrok = otroci;
    }

    // Setrji

    public void setSteviloOdraslih(int odrasli) {
        this.steviloOdraslih = odrasli;
    }
    public void setSteviloOtrok(int otroci) {
        this.steviloOtrok = otroci;
    }

    // Getterji
    public int getSteviloOdraslih() {
        return steviloOdraslih;
    }
    public int getSteviloOtrok() {
        return steviloOtrok;
    }
    public int getIdTermina() {
        return idTermina;
    }
    public String getUporabniskoImeUporabnika() {
        return uporabniskoImeUporabnika;
    }
    public String getPriimekUporabnika() {
        return priimekUporabnika;
    }
    public String getImeUporabnika() {
        return imeUporabnika;
    }
    public String getOrmNiz() {
        return  this.idTermina + "," + this.uporabniskoImeUporabnika + "," + this.imeUporabnika + "," + this.priimekUporabnika + "," + this.steviloOdraslih + "," + this.steviloOtrok;
    }

    public String nizRezervacije() {
        return "Rezerviral: " + this.imeUporabnika + " " + this.priimekUporabnika +"(" + this.uporabniskoImeUporabnika + ")" +" Število Oseb: " + this.steviloOdraslih + " Število otrok: " + this.steviloOtrok;
    }

    // Izpis
    public void izpisiRezervacijo(int stRezervacije) {
        System.out.println("Rezervacija št. " + stRezervacije + ": "+ nizRezervacije());
    }
}

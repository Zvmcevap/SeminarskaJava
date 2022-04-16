import java.io.IOException;

public class Larp extends Pocitnice {

    private String tematika;
    private boolean lastnaOprema;

    public Larp(int idPocitnic) {
        super(idPocitnic);
        this.tematika = "Placdržač";
        this.lastnaOprema = false;
    }

    public Larp(int idPocitnic, String drzava, double cena, int maxOseb, String tematika, boolean lastnaOprema) {
        super(idPocitnic, drzava, cena, maxOseb);
        this.tematika = tematika;
        this.lastnaOprema = lastnaOprema;
    }

    // Sets

    public void setOpcija1(TuristicnaAgencija ta) {
        this.lastnaOprema = !this.lastnaOprema;
        ta.getN().naslov("Lastna Oprema spremenjeno v " + ((this.lastnaOprema) ? "DA" : "NE"));
    }

    public void setOpcija2(TuristicnaAgencija ta) throws IOException {
        String izbira = " ";
        while (true) {
            ta.getN().podnaslov("Vpiši tematiko ali k za preklic");
            izbira = ta.getN().preberiNiz();
            if (izbira.equals("k")) {
                ta.getN().naslov("Urejanje Tematike Larpa Preklicano! :O");
                return;
            }
            if (!izbira.equals(" ")) {
                this.tematika = izbira;
                ta.getN().naslov("Tematika Larpa Spremenjena v: " + this.tematika);
                return;
            }
            ta.getN().podnaslov("PVVN Exception (Probajte-Vpisat-Vsaj-Nekaj Exception)");
        }
    }

    // Gets

    public boolean getLastnaOprema() {
        return this.lastnaOprema;
    }

    public String getTematika() {
        return this.tematika;
    }

    public String getOrmNiz() {
        return "1," + getIdPocitnic() + "," + getDrzava() + "," + getCena() + "," + getMaxOseb() + "," + this.tematika + "," + this.lastnaOprema;
    }

    public void izpisiPocitnice(boolean admin) {
        super.izpisiPocitnice(admin);

        String niz2 = "Tematika: " + this.getTematika() + "\tLastna Oprema: " + (getLastnaOprema() ? "DA" : "NE");

        System.out.println(niz2);
        System.out.println("-".repeat(10));
    }
}
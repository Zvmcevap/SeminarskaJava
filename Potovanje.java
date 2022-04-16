public class Potovanje extends Pocitnice {

    private boolean bazen;
    private boolean zajtrk;

    public Potovanje(int idPocitnic) {
        super(idPocitnic);
        this.bazen = false;
        this.zajtrk = false;
    }

    public Potovanje(int idPocitnic, String drzava, double cena, int maxOseb, boolean bazen, boolean zajtrk) {

        super(idPocitnic, drzava, cena, maxOseb);
        this.bazen = bazen;
        this.zajtrk = zajtrk;

    }
    // Settings
    public void setOpcija1(TuristicnaAgencija ta) {
        this.bazen = !this.bazen;
        ta.getN().naslov("Potovanje vključuje bazen: " + (this.bazen? "DA":"NE"));
    }

    public void setOpcija2(TuristicnaAgencija ta) {
        this.zajtrk = !this.zajtrk;
        ta.getN().naslov("Potovanje vključuje zajtrk: " + (this.bazen? "DA":"NE"));
    }

    // Gettings

    public boolean getBazen() {
        return this.bazen;
    }

    public boolean getZajtrk() {
         return this.zajtrk;
    }

    public String getOrmNiz() {
        return "0," + getIdPocitnic() + "," + getDrzava() + "," + getCena() + "," + getMaxOseb() + "," + this.bazen + "," + this.zajtrk;
    }

    public void izpisiPocitnice(boolean admin) {
        super.izpisiPocitnice(admin);

        String niz2 = "Vključeni: Bazen - " + (this.getBazen() ? "DA" : "NE") + " | Zajtrk - " + (this.getZajtrk() ? "DA" : "NE");

        System.out.println(niz2);
        System.out.println("-".repeat(10));
    }
}
public class Uporabnik {

    private String ime;
    private String priimek;

    private String uporabniskoIme;
    private String geslo;
    private boolean admin;

    public Uporabnik(String ime, String priimek, String uporabniskoIme, String geslo) {

        this.ime = ime;
        this.priimek = priimek;
        this.uporabniskoIme = uporabniskoIme;
        this.geslo = geslo;
        this.admin = false;

    }

    // Setterji

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public void setUporabniskoIme(String uporabniskoIme) {
        this.uporabniskoIme = uporabniskoIme;
    }

    public void setGeslo(String geslo) {
        this.geslo = geslo;
    }
    // Getterji

    public boolean getAdmin() {
        return this.admin;
    }

    public String getIme() {
        return this.ime;
    }

    public String getPriimek() {
        return this.priimek;
    }

    public String getUporabniskoIme() {
        return this.uporabniskoIme;
    }

    public String getGeslo() {
        return this.geslo;
    }


    public String nizUporabnika() {
        return "Uporabnik: " + this.uporabniskoIme + " | Ime in Priimek: " + this.ime + " " + this.priimek + " | Admin: " + (this.admin ? "Da" : "Ne");
    }

    public void izpisiUporabnika() {
        System.out.println(nizUporabnika());
    }

}

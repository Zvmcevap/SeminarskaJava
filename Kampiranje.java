import java.io.IOException;

public class Kampiranje extends Pocitnice{

    private static final String[] seznamTipov = {"Gorsko", "Gozdno", "Predmestno", "Mestno"};
    private int stTipa;

    public Kampiranje(int idPocitnic) {
        super(idPocitnic);
        this.stTipa = 0;
    }

    public Kampiranje(int idPocitnic, String drzava, double cena, int maxOseb, int stTipa){
        super(idPocitnic, drzava, cena, maxOseb);
        this.stTipa = stTipa;
    }

    // Setsit
    public void setOpcija1(TuristicnaAgencija ta) throws IOException {
        int izbira = 0;
        while (izbira < 1 || izbira > 4) {
         ta.getN().naslov("SPREMENI TIP KAMPIRANJA");
         System.out.println("TIPI: 1 - Gorsko, 2 - Gozdno, 3 - Predmestno, 4 - Mestno");
         ta.getN().podnaslov("Vpišite št. tipa: ");
         izbira = ta.getN().preberiInt();
        }
        this.stTipa = izbira - 1;
        ta.getN().naslov("Tip Kampiranja Spremenjen v: " + this.getTipKampiranja());
    }

    // Getsit
    public String getTipKampiranja(){
        return seznamTipov[stTipa];
    }
    public String getOrmNiz() {
        return "3," + getIdPocitnic() + "," + getDrzava() + "," + getCena() + "," + getMaxOseb() + "," + this.stTipa;
    }

    public void izpisiPocitnice(boolean admin) {

        super.izpisiPocitnice(admin);
        String niz2 = "Tip Kampiranja - " + this.getTipKampiranja();

        System.out.println(niz2);
        System.out.println("-".repeat(10));
    }

}
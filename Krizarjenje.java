import java.io.IOException;
import java.util.ArrayList;

public class Krizarjenje extends Pocitnice {

    private final ArrayList<String> obiskaneDrzave;
    private boolean kockanje;

    public Krizarjenje(int idPocitnic) {
        super(idPocitnic);
        this.obiskaneDrzave = new ArrayList<String>();
        this.kockanje = false;
    }

    public Krizarjenje(int idPocitnic, String drzava, double cena, int maxOseb, ArrayList<String> drzave, boolean kockanje) {

        super(idPocitnic, drzava, cena, maxOseb);
        this.obiskaneDrzave = new ArrayList<String>();
        this.obiskaneDrzave.addAll(drzave);
        this.kockanje = kockanje;

    }

    // Setem

    public void setOpcija1(TuristicnaAgencija ta) {
        this.kockanje = !this.kockanje;
        ta.getN().naslov("Kockanje spremenjeno v " + ((this.kockanje) ? "DA" : "NE"));
    }

    public void setOpcija2(TuristicnaAgencija ta) throws IOException {
        String izbira = "";

        while (true) {
            ta.getN().naslov("SPREMENI OBISKANE DRŽAVE");
            ta.getN().opcije(new String[]{"p - piši", "b - briši", "k - končaj"});
            izbira = ta.getN().preberiNiz();
            if (izbira.equals("k")) {
                ta.getN().naslov("Urejanje Držav Končano");
                return;
            }
            if (izbira.equals("p")) {
                while (!izbira.equals("n")) {
                    String nizdrzave = this.getNizObiskanihDrzav();
                    ta.getN().podnaslov("Obiskane države (brez države počitnic)");
                    System.out.println(nizdrzave + "\n");
                    ta.getN().podnaslov("Vpišite državo ki jo želite dodati ali n za nazaj");
                    izbira = " ";
                    while (izbira.equals(" ")) {
                        izbira = ta.getN().preberiNiz();
                    }
                    if (!izbira.equals("n")) {
                        int indexDrzave = 0;
                        while (indexDrzave < 1) {
                            ta.getN().podnaslov("Vpišite št. mesta na katero želite vpisati novo državo");
                            indexDrzave = ta.getN().preberiInt();
                            if (indexDrzave > this.obiskaneDrzave.size() + 1) {
                                indexDrzave = this.obiskaneDrzave.size() + 1;
                            }
                        }
                        this.obiskaneDrzave.add(indexDrzave - 1, izbira);
                    }
                }
            }
            if (izbira.equals("b")) {
                boolean izbrisDrzav = (this.obiskaneDrzave.size() != 0);
                while (izbrisDrzav) {
                    String nizdrzave = this.getNizObiskanihDrzav();
                    ta.getN().podnaslov("Obiskane države (brez države počitnic)");
                    System.out.println(nizdrzave + "\n");
                    ta.getN().podnaslov("Vpišite državo ali št. države, ki jo želite izbrisati ali n za nazaj");
                    izbira = ta.getN().preberiNiz();
                    boolean jeInteger = false;
                    int stDrzave = 0;
                    int steviloDrzav = this.obiskaneDrzave.size();
                    try {
                        stDrzave = Integer.parseInt(izbira);
                        jeInteger = true;
                    } catch (NumberFormatException ignored) {
                    }
                    if (jeInteger) {
                        if (stDrzave > 0 && stDrzave <= this.obiskaneDrzave.size()) {
                            this.obiskaneDrzave.remove(stDrzave - 1);
                            ta.getN().podnaslov("Država izbrisana");
                        }
                    } else {
                        this.obiskaneDrzave.remove(izbira);
                    }
                    if (!izbira.equals("n") && steviloDrzav == this.obiskaneDrzave.size()) {
                        ta.getN().podnaslov("Nepravilen vnos, nič izbrisano\n");
                    }
                    if (izbira.equals("n") || this.obiskaneDrzave.size() == 0) {
                        izbrisDrzav = false;
                    }
                }
                if (this.obiskaneDrzave.size() == 0) {
                    ta.getN().naslov("Ni držav za izbrisati");
                }
            }
        }
    }

    public void addObiskaneDrzave(String drzava) {
        this.obiskaneDrzave.add(drzava);
    }

    public void removeObiskaneDrzave(String drzava) {
        this.obiskaneDrzave.remove(drzava);
    }


    // Getem

    public ArrayList<String> getObiskaneDrzave() {
        return this.obiskaneDrzave;
    }

    public boolean getKockanje() {
        return this.kockanje;
    }

    public String getOrmNiz() {
        String drzave = String.join(":", this.obiskaneDrzave);
        return "2," + getIdPocitnic() + "," + getDrzava() + "," + getCena() + "," + getMaxOseb() + "," + drzave + "," + this.kockanje;
    }

    public String getNizObiskanihDrzav(
    ) {
        String niz1 = String.join(" -> ", this.obiskaneDrzave);
        niz1 = this.getDrzava() + " -> " + niz1;
        niz1 += "\n";
        int stZvezdic = (this.getDrzava().length() - 1) / 2;
        String zvezdice = "*".repeat(stZvezdic);

        StringBuilder niz2 = new StringBuilder();
        if (this.getDrzava().length() % 2 == 0) {
            niz2.append(zvezdice).append("*").append(0).append(zvezdice);
        } else {
            niz2.append(zvezdice).append(0).append(zvezdice);
        }
        niz2.append(" -> ");
        for (int i = 0; i < this.obiskaneDrzave.size(); i++) {
            stZvezdic = (this.obiskaneDrzave.get(i).length() - 1) / 2;

            zvezdice = "*".repeat(stZvezdic);

            if (this.obiskaneDrzave.get(i).length() % 2 == 0) {
                niz2.append(zvezdice).append("*").append(i + 1).append(zvezdice);
            } else {
                niz2.append(zvezdice).append(i + 1).append(zvezdice);
            }
            if (i < this.obiskaneDrzave.size() - 1) {
                niz2.append(" -> ");
            }
        }
        return niz1 + niz2;
    }

    public void izpisiPocitnice(boolean admin) {

        super.izpisiPocitnice(admin);
        String niz2 = this.getNizObiskanihDrzav();
        String niz3 = "Kockanje v Mednarodnih vodah - " + (this.getKockanje() ? "DA" : "NE");
        System.out.println("Obiskane države: ");
        System.out.println(niz2);
        System.out.println(niz3);
        System.out.println("-".repeat(10));
    }
}
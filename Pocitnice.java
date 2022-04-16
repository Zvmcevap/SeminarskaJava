import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class Pocitnice {
    private final int idPocitnic;
    private String drzava;
    private double cena;

    private int maxOseb;

    private final ArrayList<Termin> seznamTerminov;
    private final ArrayList<Rezervacija> seznamRezervacij;
    private LocalDateTime najzgodnejsiOdhod;

    public Pocitnice(int idPocitnic) {
        this.idPocitnic = idPocitnic;
        this.drzava = "Placdržač";
        this.cena = 0;
        this.maxOseb = 0;

        this.seznamTerminov = new ArrayList<Termin>();
        this.seznamRezervacij = new ArrayList<Rezervacija>();
        this.najzgodnejsiOdhod = null;
    }

    public Pocitnice(int idPocitnic, String drzava, double cena, int maxOseb) {

        this.idPocitnic = idPocitnic;
        this.drzava = drzava;
        this.cena = cena;
        this.maxOseb = maxOseb;

        this.seznamTerminov = new ArrayList<Termin>();
        this.seznamRezervacij = new ArrayList<Rezervacija>();
        this.najzgodnejsiOdhod = null;

    }

    // set metodote

    public void setDrzava(TuristicnaAgencija ta) throws IOException {
        ta.getN().naslov("SPREMEMBA DRŽAVE");
        ta.getN().podnaslov("Vpiši Državo ali n za nazaj");
        String vnos = ta.getN().preberiNiz();
        while (vnos.equals(" ")) {
            ta.getN().podnaslov("Vpiši državo ali n za nazaj, prazni vnosi bodo vrteli to zanko v nedogled!");
            vnos = ta.getN().preberiNiz();
        }
        if (!vnos.equals("n")) {
            this.drzava = vnos;
        }
    }

    public void setCena(TuristicnaAgencija ta) throws IOException {
        ta.getN().naslov("SPREMEMBA CENE");
        double novaCena = 0;
        while (novaCena == 0) {
            ta.getN().podnaslov("Vpiši ceno (0 ali manj ne more bit (Kapitalizem \\o/)) ali n za nazaj");
            String vnos = ta.getN().preberiNiz();
            if (vnos.equals("n")) {
                return;
            }
            try {
                novaCena = Double.parseDouble(vnos);
            } catch (NumberFormatException ignored) {
            }
            if (novaCena > 0.0) {
                this.cena = novaCena;
                return;
            }
            ta.getN().podnaslov("Probaj številko, probaj večjo od 0 ;)");
        }
    }

    public void setMaxOseb(TuristicnaAgencija ta) throws IOException {
        ta.getN().naslov("SPREMEMBA MAXIMALNEGA ŠTEVILA OSEB NA TERMIN");
        int novMax = 0;
        while (novMax == 0) {
            ta.getN().podnaslov("Vpiši Max Oseb (0 ali manj ni logično) ali n za nazaj");
            String vnos = ta.getN().preberiNiz();
            if (vnos.equals("n")) {
                return;
            }
            try {
                novMax = Integer.parseInt(vnos);
            } catch (NumberFormatException ignored) {
            }
            if (novMax > 0) {
                this.maxOseb = novMax;
                return;
            }
            ta.getN().podnaslov("Probaj številko, probaj večjo od 0 ;)");
        }
    }

    public void ustvariTermin(TuristicnaAgencija ta) throws IOException {
        boolean napaka = false;
        String nizNapake = "";
        int idTermina = 1;
        if (this.seznamTerminov.size() > 0) {
            idTermina = this.seznamTerminov.get(this.seznamTerminov.size() - 1).getIdTermina() + 1;
        }
        String odhod = "NEDOLOČEN";
        String prihod = "NEDOLOČEN";
        boolean datumiNarobe = true;
        while (true) {
            ta.getN().naslov("USTVARI NOV TERMIN");
            System.out.println("ODHOD: " + odhod + " | PRIHOD: " + prihod);
            if (datumiNarobe) {
                System.out.println(" *-*-* ODHOD mora bit pred PRIHOD-om *-*-* ");
            }
            System.out.println("");
            if (napaka) {
                ta.getN().nizNapaka(nizNapake);
            }
            ta.getN().opcije(new String[]{"o - nastavi odhod", "p - nastavi prihod", "k - potrdi in končaj", "n - obupaj"});
            char izbira = ta.getN().preberiNiz().charAt(0);
            switch (izbira) {
                case 'o':
                    napaka = false;
                    ta.getN().naslov("VPIŠI ODHOD");
                    odhod = ta.getN().dobiDatum(true);
                    LocalDateTime odhodDatum = LocalDateTime.parse(odhod, ta.getFormatter());
                    if (prihod.equals("NEDOLOČEN")) {
                        break;
                    } else {
                        LocalDateTime prihodDatum = LocalDateTime.parse(prihod, ta.getFormatter());
                        if (odhodDatum.isBefore(prihodDatum)) {
                            datumiNarobe = false;
                            break;
                        }
                    }
                    break;
                case 'p':
                    napaka = false;
                    ta.getN().naslov("VPIŠI PRIHOD");
                    prihod = ta.getN().dobiDatum(true);
                    LocalDateTime prihodDatum = LocalDateTime.parse(prihod, ta.getFormatter());
                    if (odhod.equals("NEDOLOČEN")) {
                        break;
                    } else {
                        odhodDatum = LocalDateTime.parse(prihod, ta.getFormatter());
                        if (odhodDatum.isBefore(prihodDatum)) {
                            datumiNarobe = false;
                            break;
                        }
                    }
                    break;
                case 'k':
                    if (odhod.equals("NEDOLOČEN") || prihod.equals("NEDOLOČEN")) {
                        napaka = true;
                        nizNapake = "Vpiši datume bitešen...";
                        datumiNarobe = true;
                        break;
                    }
                    odhodDatum = LocalDateTime.parse(odhod, ta.getFormatter());
                    prihodDatum = LocalDateTime.parse(prihod, ta.getFormatter());
                    if (odhodDatum.isBefore(prihodDatum)) {
                        Termin noviTermin = new Termin(idTermina, odhod, prihod);
                        this.addTermin(noviTermin);
                        return;
                    }
                    napaka = true;
                    nizNapake = "ODHOD mora biti STAREJŠI od PRIHODA";
                    datumiNarobe = true;
                    break;
                case 'n':
                    return;
                default:
                    napaka = true;
                    nizNapake = "OPICEEEE! Mislm, opcija napačna!";
                    break;
            }
        }
    }
    // Placdržač za opcije otrok
    public void setOpcija1(TuristicnaAgencija ta)throws IOException {}
    public void setOpcija2(TuristicnaAgencija ta)throws IOException {}


    public void addTermin(Termin noviTermin) {
        this.seznamTerminov.add(noviTermin);
        this.setNajzgodnejsiOdhod();
    }

    public void setNajzgodnejsiOdhod() {
        for (Termin t : this.seznamTerminov) {
            if (this.najzgodnejsiOdhod == null || t.getOdhod().isBefore(this.najzgodnejsiOdhod)) {
                this.najzgodnejsiOdhod = t.getOdhod();
            }
        }
    }

    public void izbrisiTermine(TuristicnaAgencija ta) throws IOException {
        boolean napaka = false;
        while (this.seznamTerminov.size() > 1) {
            this.izpisiPocSTermini(ta.getTrenutniUporabnik().getAdmin());
            Termin terminZaZbrisat = null;
            System.out.println("");
            if (napaka) {
                ta.getN().nizNapaka("Napačen vnos!!!");
            }
            ta.getN().podnaslov("Vpiši št termina ki ga hočeš izbrisati ali n za nazaj");
            String vnos = ta.getN().preberiNiz();
            int stTermina = 0;
            if (vnos.equals("n")) {
                return;
            }
            try {
                stTermina = Integer.parseInt(vnos);
            } catch (NumberFormatException ignored) {
            }
            for (Termin termin : this.seznamTerminov) {
                if (termin.getIdTermina() == stTermina) {
                    terminZaZbrisat = termin;
                }
            }
            if (terminZaZbrisat != null) {
                ArrayList<Rezervacija> placholdr = new ArrayList<>(this.seznamRezervacij);
                for (Rezervacija rez : placholdr) {
                    if (rez.getIdTermina() == terminZaZbrisat.getIdTermina()) {
                        this.seznamRezervacij.remove(rez);
                    }
                }
                this.seznamTerminov.remove(terminZaZbrisat);
                this.setNajzgodnejsiOdhod();
                napaka = false;
                ta.getN().naslov("Termin Izbrisan");

            }
            else {
                napaka = true;
            }
        }
        ta.getN().naslov("PREMALO TERMINOV DAB JIH BRISALI (Kakšnega raje dodaj..)");
    }

    public void addRezervacija(TuristicnaAgencija ta) throws IOException {
        boolean napaka = false;
        boolean rezervacija = true;
        String nizNapake = "";
        while (rezervacija) {
            this.izpisiPocSTermini(ta.getTrenutniUporabnik().getAdmin());
            System.out.println("");
            if (napaka) {
                ta.getN().nizNapaka(nizNapake);
            }
            ta.getN().opcije(new String[]{"št.termina - rezervacijo tega termina", "n - nazaj"});
            String vnos = ta.getN().preberiNiz();
            try {
                int stTer = Integer.parseInt(vnos);
                for (Termin termin : this.seznamTerminov) {
                    if (termin.getIdTermina() == stTer) {
                        Uporabnik u = ta.getTrenutniUporabnik();
                        String adminNiz = "";
                        int odrasli = termin.getZasedenostOdrasli(this.seznamRezervacij);
                        int otroci = termin.getZasedenostOtroci(this.seznamRezervacij);
                        double delezZasedenosti = (double) (odrasli + otroci) / this.getMaxOseb();

                        if (u.getAdmin()) {
                            adminNiz += "Število Oseb z Rezervacijo: " + odrasli + " Odraslih + " + otroci + " Otrok.";
                        } else {
                            if (delezZasedenosti == 0) {
                                adminNiz += " ----- ZAGOTOVLJENO";
                            } else if (delezZasedenosti < 0.5) {
                                adminNiz += " ----- SKORAJ ZAGOTOVLJENO";
                            } else {
                                adminNiz += " ----- NI ZAGOTOVLJENO";
                            }
                        }
                        termin.izpisTermin(adminNiz);
                        int stOdraslih;
                        int stOtrok;
                        ta.getN().podnaslov("Vpiši Število Odraslih");
                        stOdraslih = ta.getN().preberiInt();
                        if (stOdraslih < 1) {
                            stOdraslih = 1;
                        }
                        ta.getN().podnaslov("Vpiši Število Otrok");
                        stOtrok = ta.getN().preberiInt();
                        if (stOtrok < 0) {
                            stOdraslih = 0;
                        }
                        if ((stOdraslih + stOtrok + otroci + odrasli) > (this.maxOseb)) {
                            napaka = true;
                            nizNapake = "NI PROSTORA ZA TOLIKO OSEB!";
                        } else {
                            Rezervacija novaRezervacija = new Rezervacija(stTer, u.getUporabniskoIme(), u.getIme(), u.getPriimek(), stOdraslih, stOtrok);
                            this.seznamRezervacij.add(novaRezervacija);
                            ta.posodobiPocitnice();
                            ta.getN().podnaslov("REZERVACIJA POTRJENA");
                        }
                    }
                }
            } catch (NumberFormatException ignored) {
                if (vnos.equals("n")) {
                    rezervacija = false;
                } else {
                    napaka = true;
                    nizNapake = "NI TEGA TERMINA!";
                }
            }
        }
    }

    public void removeRezervacija(Rezervacija rezervacija) {
        this.seznamRezervacij.remove(rezervacija);
    }

    // get metodote

    public int getIdPocitnic() {
        return this.idPocitnic;
    }

    public String getDrzava() {
        return this.drzava;
    }

    public double getCena() {
        return this.cena;
    }

    public int getMaxOseb() {
        return this.maxOseb;
    }

    public ArrayList<Termin> getSeznamTerminov() {
        return this.seznamTerminov;
    }

    public ArrayList<Rezervacija> getSeznamRezervacij() {
        return this.seznamRezervacij;
    }

    public LocalDateTime getNajzgodnejsiOdhod() {
        return najzgodnejsiOdhod;
    }

    public String getOrmNiz() {
        return "";
    }

    public void izpisiPocitnice(boolean admin) {
        String niz1 = "Država: " + this.getDrzava() + "\tCena: " + this.getCena() + "€" + ((admin) ? "\tMax Oseb na Termin: " + this.getMaxOseb() : "");
        System.out.println("-".repeat(niz1.length()));
        System.out.println("\t\t" + "*".repeat(5) + "\t" + this.getClass().getSimpleName() + "\t" + "*".repeat(5));
        System.out.println("-".repeat(niz1.length()));
        if (admin) {
            System.out.println("ID: " + this.idPocitnic);
        }
        System.out.println(niz1);
    }

    public void izpisiTermine(boolean admin) {
        System.out.println("Termini: ");
        for (Termin t : this.getSeznamTerminov()) {

            int odrasli = t.getZasedenostOdrasli(this.seznamRezervacij);
            int otroci = t.getZasedenostOtroci(this.seznamRezervacij);
            double delezZasedenosti = (double) (odrasli + otroci) / this.getMaxOseb();

            String adminNiz = "";

            if (admin) {
                adminNiz += "Število Oseb z Rezervacijo: " + odrasli + " Odraslih + " + otroci + " Otrok.";
            } else {
                if (delezZasedenosti == 0) {
                    adminNiz += " ----- ZAGOTOVLJENO";
                } else if (delezZasedenosti < 0.5) {
                    adminNiz += " ----- SKORAJ ZAGOTOVLJENO";
                } else {
                    adminNiz += " ----- NI ZAGOTOVLJENO";
                }
            }
            t.izpisTermin(adminNiz);
        }
    }

    public void izpisiPocSTermini(boolean admin) {
        this.izpisiPocitnice(admin);
        this.izpisiTermine(admin);
    }

}

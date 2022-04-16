import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Filtri {
    private LocalDateTime odhod;
    private LocalDateTime prihod;

    private double cenaOd;
    private double cenaDo;

    private final ArrayList<String> drzave;
    private boolean filtrirajPodrazred;
    private final boolean[] tipPodrazreda;

    private int sortirajPoIndex;
    private final String[] sortirajPo;
    private boolean padajoce;

    public Filtri() {
        this.odhod = null;
        this.prihod = null;

        this.cenaDo = 0;
        this.cenaOd = 0;

        this.drzave = new ArrayList<String>();
        this.filtrirajPodrazred = false;
        this.tipPodrazreda = new boolean[]{false, false, false, false};

        this.sortirajPoIndex = 0;
        this.sortirajPo = new String[]{"Datumu", "Ceni", "Državi", "Tipu", "ID-ju"};
        this.padajoce = false;
    }
    // Filtriranje

    public ArrayList<Pocitnice> filterCene(ArrayList<Pocitnice> seznamPocitnic) {
        ArrayList<Pocitnice> filtriranSeznamOd = new ArrayList<Pocitnice>();
        ArrayList<Pocitnice> filtriranSeznam = new ArrayList<Pocitnice>();

        if (this.cenaOd != 0) {
            for (Pocitnice poc : seznamPocitnic) {
                if (poc.getCena() >= this.cenaOd) {
                    filtriranSeznamOd.add(poc);
                }
            }
        } else {
            filtriranSeznamOd.addAll(seznamPocitnic);
        }
        if (this.cenaDo != 0) {
            for (Pocitnice poc : filtriranSeznamOd) {
                if (poc.getCena() <= this.cenaDo) {
                    filtriranSeznam.add(poc);
                }
            }
        } else {
            filtriranSeznam.addAll(filtriranSeznamOd);
        }
        return filtriranSeznam;
    }

    public ArrayList<Pocitnice> filterDrzave(ArrayList<Pocitnice> seznamPocitnic) {
        ArrayList<Pocitnice> filtriranSeznam = new ArrayList<Pocitnice>();
        if (this.drzave.size() != 0) {
            for (Pocitnice poc : seznamPocitnic) {
                if (this.drzave.contains(poc.getDrzava())) {
                    filtriranSeznam.add(poc);
                }
            }
        } else {
            filtriranSeznam.addAll(seznamPocitnic);
        }
        return filtriranSeznam;
    }

    public ArrayList<Pocitnice> filterTipa(ArrayList<Pocitnice> seznamPocitnic) {
        ArrayList<Pocitnice> filtriranSeznam = new ArrayList<Pocitnice>();
        if (this.filtrirajPodrazred) {

            for (Pocitnice poc : seznamPocitnic) {
                if (this.tipPodrazreda[0] && poc.getClass().getSimpleName().equals("Potovanje")) {
                    filtriranSeznam.add(poc);
                }
                if (this.tipPodrazreda[1] && poc.getClass().getSimpleName().equals("Larp")) {
                    filtriranSeznam.add(poc);
                }
                if (this.tipPodrazreda[2] && poc.getClass().getSimpleName().equals("Krizarjenje")) {
                    filtriranSeznam.add(poc);
                }
                if (this.tipPodrazreda[3] && poc.getClass().getSimpleName().equals("Kampiranje")) {
                    filtriranSeznam.add(poc);
                }
            }
        } else {
            filtriranSeznam.addAll(seznamPocitnic);
        }
        return filtriranSeznam;
    }

    public ArrayList<Pocitnice> filterCasa(ArrayList<Pocitnice> seznamPocitnic) {
        ArrayList<Pocitnice> filtriranSeznam = new ArrayList<Pocitnice>();
        ArrayList<Pocitnice> filtriranOdhod = new ArrayList<Pocitnice>();

        if (this.odhod != null) {
            for (Pocitnice poc : seznamPocitnic) {
                boolean obstajaTermin = false;
                for (Termin t : poc.getSeznamTerminov()) {
                    if (t.getOdhod().isAfter(this.odhod) || t.getOdhod().equals(this.odhod)) {
                        obstajaTermin = true;
                    }
                }
                if (obstajaTermin) {
                    filtriranOdhod.add(poc);
                }
            }
        } else {
            filtriranOdhod.addAll(seznamPocitnic);
        }
        if (this.prihod != null) {
            for (Pocitnice poc : filtriranOdhod) {
                boolean obstajaTermin = false;
                for (Termin t : poc.getSeznamTerminov()) {
                    if (t.getPrihod().isBefore(this.prihod) || t.getPrihod().equals(this.prihod)) {
                        obstajaTermin = true;
                    }
                }
                if (obstajaTermin) {
                    filtriranSeznam.add(poc);
                }
            }
        } else {
            filtriranSeznam.addAll(filtriranOdhod);
        }
        return filtriranSeznam;
    }

    public ArrayList<Pocitnice> sortirajSeznam(ArrayList<Pocitnice> seznamPocitnic) {
        ArrayList<Pocitnice> sortiranSeznam = new ArrayList<Pocitnice>(seznamPocitnic);
        if (this.sortirajPoIndex == 0) {
            if (padajoce) {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getNajzgodnejsiOdhod).reversed());
            } else {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getNajzgodnejsiOdhod));
            }
        }
        if (this.sortirajPoIndex == 1) {
            if (padajoce) {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getCena).reversed());
            } else {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getCena));
            }
        }
        if (this.sortirajPoIndex == 2) {
            if (padajoce) {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getDrzava).reversed());
            } else {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getDrzava));
            }
        }
        if (this.sortirajPoIndex == 3) {
            if (padajoce) {
                sortiranSeznam.sort(Comparator.comparing(x -> x.getClass().getSimpleName()).reversed());
            } else {
                sortiranSeznam.sort(Comparator.comparing(x -> x.getClass().getSimpleName()));
            }
        }
        if (this.sortirajPoIndex == 4) {
            if (padajoce) {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getIdPocitnic).reversed());
            } else {
                sortiranSeznam.sort(Comparator.comparing(Pocitnice::getIdPocitnic));
            }
        }

        return sortiranSeznam;
    }

    public ArrayList<Pocitnice> koncniFiltriranSeznam(ArrayList<Pocitnice> seznamPocitnic) {
        // Filter Časa
        ArrayList<Pocitnice> filtriranSeznam = this.filterCasa(seznamPocitnic);
        // Filter Cene
        filtriranSeznam = this.filterCene(filtriranSeznam);
        // Filter Države
        filtriranSeznam = this.filterDrzave(filtriranSeznam);
        // Filter Tipa
        filtriranSeznam = this.filterTipa(filtriranSeznam);
        // Sortiraj na koncu in vrni
        return this.sortirajSeznam(filtriranSeznam);
    }


    // Setterji Filtrov
    public void setCenaDo(double cenaDo) {
        this.cenaDo = cenaDo;
    }

    public void setCenaOd(double cenaOd) {
        this.cenaOd = cenaOd;
    }

    public void setOdhod(LocalDateTime odhod) {
        this.odhod = odhod;
    }

    public void setPrihod(LocalDateTime prihod) {
        this.prihod = prihod;
    }

    public void toggleTipPodrazreda(int indexTipa) {
        this.tipPodrazreda[indexTipa] = !this.tipPodrazreda[indexTipa];
    }

    public void resetTipPodrazreda() {
        Arrays.fill(this.tipPodrazreda, false);
    }

    public void toggleFiltrirajPodrazred() {
        this.filtrirajPodrazred = !filtrirajPodrazred;
    }

    public void setFiltrirajPodrazred(boolean bol) {
        this.filtrirajPodrazred = bol;
    }

    public void addDrzava(String drzava) {
        if (this.drzave.contains(drzava)) {
            System.out.println("****** DRŽAVA ŽE NA SEZNAMU ******");
        } else {
            this.drzave.add(drzava);
        }
    }

    public void removeDrzava(String drzava) {
        if (this.drzave.contains(drzava)) {
            this.drzave.remove(drzava);
        } else {
            System.out.println("****** DRŽAVE NI NA SEZNAMU ******");
        }
    }

    public void resetDrzave() {
        this.drzave.clear();
    }

    // Getterji Filtrov

    public boolean isPadajoce() {
        return padajoce;
    }

    public String getSortiranjePo() {
        return this.sortirajPo[this.sortirajPoIndex];
    }

    public ArrayList<String> getDrzave() {
        return drzave;
    }

    public boolean isFiltrirajPodrazred() {
        return filtrirajPodrazred;
    }

    public boolean[] getTipPodrazreda() {
        return tipPodrazreda;
    }

    public double getCenaDo() {
        return cenaDo;
    }

    public double getCenaOd() {
        return cenaOd;
    }

    public LocalDateTime getOdhod() {
        return odhod;
    }

    public LocalDateTime getPrihod() {
        return prihod;
    }

    public String getNizCena() {
        return "Cena od: " + ((this.cenaOd == 0) ? "NI DOLOČENO" : this.cenaOd) + " - do: " + ((this.cenaDo == 0) ? "NI DOLOČENO" : this.cenaDo);
    }

    public String getNizDrzave() {
        return "Države: " + ((this.drzave.size() == 0) ? "VSE" : String.join(", ", this.drzave));
    }

    public String getNizTipi() {
        String nizTipi = "";
        if (this.tipPodrazreda[0]) {
            nizTipi += "POTOVANJE";
        }
        if (this.tipPodrazreda[1]) {
            nizTipi += (nizTipi.length() == 0) ? "LARP" : ", LARP";
        }
        if (this.tipPodrazreda[2]) {
            nizTipi += (nizTipi.length() == 0) ? "KRIŽARJENJE" : ", KRIŽARJENJE";
        }
        if (this.tipPodrazreda[3]) {
            nizTipi += (nizTipi.length() == 0) ? "KAMPIRANJE" : ", KAMPIRANJE";
        }
        return nizTipi;
    }

    public void nastaviFiltre(TuristicnaAgencija ta) throws IOException {
        Nizi n = ta.getN();
        boolean napaka = false;
        String nizNapake = "";
        boolean dolociFiltre = true;
        char izbira;
        while (dolociFiltre) {
            n.izpisiFiltre(ta);
            n.opcije(new String[]{"d - določi datum", "c - določi ceno", "w - določi dRžave", "t - določi tipe", "s - sortiranje", "r - resetiraj vse filtre", "k - končaj"});
            if (napaka) {
                n.nizNapaka(nizNapake);
            }
            izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 's':
                    napaka = false;
                    int indexSortinga = 0;
                    while (indexSortinga == 0) {
                        System.out.println("SORTIRAJ PO: ");
                        System.out.println("1 - Datumu, 2 - Ceni, 3 - Državi, 4 - Tipu počitnic" + (ta.getTrenutniUporabnik().getAdmin() ? ", 5 - ID-ju" : ""));
                        n.podnaslov("Vpiši eno od številk");
                        indexSortinga = n.preberiInt();
                        if (!ta.getTrenutniUporabnik().getAdmin() && indexSortinga == 5 || indexSortinga > 5 || indexSortinga < 0) {
                            indexSortinga = 0;
                        } else {
                            this.sortirajPoIndex = indexSortinga - 1;
                        }
                    }
                    indexSortinga = 0;
                    while (indexSortinga == 0) {
                        System.out.println("");
                        System.out.println("1 - VZHAJAJOČE, 2 - PADAJOČE");
                        n.podnaslov("Vpiši 1 ali 2");
                        indexSortinga = n.preberiInt();
                        if (indexSortinga == 1) {
                            this.padajoce = false;
                        } else if (indexSortinga == 2) {
                            this.padajoce = true;
                        } else {
                            indexSortinga = 0;
                        }
                    }
                    break;
                case 't':
                    napaka = false;
                    boolean dolociTipeLoop = true;
                    while (dolociTipeLoop) {
                        String nizPotovanje = "1 - POTOVANJE: " + (ta.getFilter().getTipPodrazreda()[0] ? "DA" : "NE") + "\t";
                        String nizLarp = "2 - LARP: " + (ta.getFilter().getTipPodrazreda()[1] ? "DA" : "NE") + "\t";
                        String nizKrizarjenje = "3 - KRIZARJENJE: " + (ta.getFilter().getTipPodrazreda()[2] ? "DA" : "NE") + "\t";
                        String nizKampiranje = "4 - KAMPIRANJE: " + (ta.getFilter().getTipPodrazreda()[3] ? "DA" : "NE");
                        n.naslov("Filtriraj Tipe Počitnic");
                        System.out.println("v - vključi/izključi filter: " + (ta.getFilter().isFiltrirajPodrazred() ? "DA" : "NE"));
                        System.out.println("");
                        System.out.println(nizPotovanje + nizLarp + nizKrizarjenje + nizKampiranje);
                        System.out.println("k - končaj");
                        if (napaka) {
                            n.nizNapaka(nizNapake);
                        }
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'v':
                                napaka = false;
                                ta.getFilter().toggleFiltrirajPodrazred();
                                ta.getFilter().resetTipPodrazreda();
                                break;
                            case 'k':
                                dolociTipeLoop = false;
                                napaka = false;
                                break;
                            default:
                                try {
                                    int indexTipa = Integer.parseInt(String.valueOf(izbira)) - 1;
                                    if (indexTipa < 0 || indexTipa > 3) {
                                        napaka = true;
                                        nizNapake = "Te izbire tudi ni";
                                    } else {
                                        ta.getFilter().toggleTipPodrazreda(indexTipa);
                                        ta.getFilter().setFiltrirajPodrazred(true);
                                    }
                                } catch (NumberFormatException ignored) {
                                    napaka = true;
                                    nizNapake = "Te izbire ni!";
                                }
                                break;
                        }
                    }
                    break;
                case 'd':
                    napaka = false;
                    boolean dolociDatum = true;
                    while (dolociDatum) {
                        String nizOdhod = "Datum od: " + ((ta.getFilter().getOdhod() == null) ? "NI DOLOČEN" : ta.getFilter().getOdhod().format(ta.getFormatter()));
                        String nizPrihod = " - do: " + ((ta.getFilter().getPrihod() == null) ? "NI DOLOČEN" : ta.getFilter().getPrihod().format(ta.getFormatter()));
                        System.out.println(nizOdhod + nizPrihod);

                        n.opcije(new String[]{"o - nastavi od", "d - nastavi do", "r - resetiraj filter časa", "k - končaj"});
                        if (napaka) {
                            n.nizNapaka(nizNapake);
                        }
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'o':
                                napaka = false;
                                String nizO = n.dobiDatum(false);
                                LocalDateTime odhod = LocalDateTime.parse(nizO, ta.getFormatter());
                                ta.getFilter().setOdhod(odhod);
                                break;
                            case 'd':
                                napaka = false;
                                String nizP = n.dobiDatum(false);
                                LocalDateTime prihod = LocalDateTime.parse(nizP, ta.getFormatter());
                                ta.getFilter().setPrihod(prihod);
                                break;
                            case 'r':
                                napaka = false;
                                ta.getFilter().setOdhod(null);
                                ta.getFilter().setPrihod(null);
                                break;
                            case 'k':
                                napaka = false;
                                dolociDatum = false;
                                break;
                            default:
                                napaka = true;
                                nizNapake = "Napačna Opcija Seveda";
                                break;
                        }

                    }
                    break;
                case 'r':
                    napaka = false;
                    ta.resetirajVseFiltre();
                    break;
                case 'c':
                    napaka = false;
                    System.out.println("Vpišite Ceno od (0 ali napačen vnos izbriše filter): ");
                    double cenaOd = n.preberiDouble();
                    System.out.println("Vpišite Ceno do (0 ali napačen vnos izbriše filter): ");
                    double cenaDo = n.preberiDouble();
                    ta.getFilter().setCenaDo(cenaDo);
                    ta.getFilter().setCenaOd(cenaOd);
                    break;
                case 'w':
                    napaka = false;
                    boolean nastaviDrzave = true;
                    while (nastaviDrzave) {
                        System.out.println("Nastavi filter " + ta.getFilter().getNizDrzave());
                        n.opcije(new String[]{"v - vpiši države", "b - briši države", "r - resetiraj filter", "k - končaj"});
                        if (napaka) {
                            n.nizNapaka(nizNapake);
                        }
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'v':
                                napaka = false;
                                boolean vpisiDrzaveloop = true;
                                while (vpisiDrzaveloop) {
                                    n.naslov("VPISOVANJE DRŽAV");
                                    n.podnaslov(ta.getFilter().getNizDrzave());
                                    System.out.println("");
                                    n.podnaslov("Vpiši Ime Države ali k za končaj: ");
                                    String drzava = n.preberiNiz();
                                    if (drzava.equals("k")) {
                                        vpisiDrzaveloop = false;
                                    } else if (!drzava.equals(" ")) {
                                        ta.getFilter().addDrzava(drzava);
                                    }
                                }
                                break;
                            case 'b':
                                napaka = false;
                                boolean izbrisDrzav = true;
                                while (izbrisDrzav) {
                                    n.naslov("IZBRIS DRŽAV");
                                    n.podnaslov(ta.getFilter().getNizDrzave());
                                    System.out.println("");
                                    n.podnaslov("Vpiši Ime Države ali k za končaj: ");
                                    String drzava = n.preberiNiz();
                                    if (drzava.equals("k")) {
                                        izbrisDrzav = false;
                                    } else if (!drzava.equals(" ")) {
                                        ta.getFilter().removeDrzava(drzava);
                                    }
                                }
                                break;
                            case 'r':
                                napaka = false;
                                ta.getFilter().resetDrzave();
                                n.podnaslov("Filter Resetiran!");
                                break;
                            case 'k':
                                napaka = false;
                                nastaviDrzave = false;
                                break;
                            default:
                                napaka = true;
                                nizNapake = "Te Opice Ni Na Seznamu";
                                break;
                        }
                    }
                    break;
                case 'k':
                    napaka = false;
                    dolociFiltre = false;
                    break;
                default:
                    napaka = true;
                    nizNapake = "Izbrali ste napačno opico :P";
            }
        }
    }
}
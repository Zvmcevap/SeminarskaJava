import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class TuristicnaAgencija {

    private final ArrayList<Pocitnice> seznamPocitnic;
    private final ArrayList<Uporabnik> seznamUporabnikov;
    private Uporabnik trenutniUporabnik;
    private final Orm orm = new Orm();
    private Filtri filter = new Filtri();
    private final Nizi n = new Nizi();


    public TuristicnaAgencija() throws IOException {
        // Upam da bo Orm mal izboljšal berljivost....
        this.seznamPocitnic = this.orm.getSeznamPocitnic();
        this.seznamUporabnikov = this.orm.getSeznamUporabnikov();
    }

    // Upravljanje z Uporabniki
    // Setterji

    public void izbrisiUporabnika() throws IOException {
        boolean napaka = false;
        String niznapake = "";
        String izbira = " ";
        while (true) {
            Uporabnik uporabnikZaIzbris = null;
            if (this.seznamUporabnikov.size() <= 1) {
                n.naslov("SAMO TI OBSTAJAŠ! Moraš ustvariti novega uporabnika, da ga lahko izbrišeš");
                return;
            }
            n.naslov("BRISANJE UPORABNIKOV");
            for (Uporabnik uporabnik : this.seznamUporabnikov) {
                uporabnik.izpisiUporabnika();
            }
            System.out.println("");
            if (napaka) {
                n.nizNapaka(niznapake);
            }
            n.podnaslov("Vpišite Uporabniško ime uporabnika, ki ga želite izbrisati ali k za končaj");
            izbira = n.preberiNiz();
            if (izbira.equals("k")) {
                n.naslov("BRISANJE UPORABNIKOV KONČANO");
                return;
            }
            for (Uporabnik uporabnik : this.seznamUporabnikov) {
                if (uporabnik.getUporabniskoIme().equals(izbira)) {
                    uporabnikZaIzbris = uporabnik;
                }
            }
            if (uporabnikZaIzbris == null) {
                napaka = true;
                niznapake = "To uporabniško ime ne obstaja.";
            }
            else if (uporabnikZaIzbris.getUporabniskoIme().equals(trenutniUporabnik.getUporabniskoIme())) {
                napaka = true;
                niznapake = "Sebe ne smeš izbrisati! (Tko pohendlam da je vsaj en administrator)";
            }
            else {
                napaka = false;
                this.seznamUporabnikov.remove(uporabnikZaIzbris);
                for (Pocitnice pocitnice: this.seznamPocitnic) {
                    ArrayList<Rezervacija> rezervacije = new ArrayList<>(pocitnice.getSeznamRezervacij());
                    for (Rezervacija rezervacija : rezervacije) {
                        if (rezervacija.getUporabniskoImeUporabnika().equals(uporabnikZaIzbris.getUporabniskoIme())) {
                            pocitnice.getSeznamRezervacij().remove(rezervacija);
                        }
                    }
                }
                this.orm.posodobiSeznamUporabnikov(this.seznamUporabnikov);
                this.posodobiPocitnice();
                System.out.println("");
                n.podnaslov("UPORABNIK IZBRISAN!");
            }
        }
    }

    // Ta pošast prijavi obstoječega uporabnika :,D
    public void prijaviUporabnika() throws IOException {
        boolean napaka = false;
        String nizNapake = "";
        Uporabnik uporabnik = null;

        n.podnaslov("Vnesi Uporabniško Ime");
        String uporabniskoIme = n.preberiNiz();
        boolean neObstaja = !this.uporabniskoImeObstaja(uporabniskoIme);
        if (this.uporabniskoImeObstaja(uporabniskoIme)) {
            uporabnik = this.zbrskajUporabnika(uporabniskoIme);
        }

        n.podnaslov("Vnesi Geslo");
        String geslo = n.preberiNiz();
        boolean gesloVidno = false;
        boolean gesloNapacno = !this.gesloPravilno(geslo, uporabnik);
        if (gesloNapacno) {
            nizNapake = "Geslo neresnično!";
        }

        boolean prijava = true;

        while (prijava) {
            n.naslov("\t\t\tPrijava");
            System.out.println("Uporabniško ime: " + uporabniskoIme + ((neObstaja) ? " --- NE OBSTAJA! ---" : ""));
            System.out.println("Geslo: " + ((gesloVidno) ? geslo : "*".repeat(geslo.length())) + ((gesloNapacno) ? " --- GESLO JE NAPAČNO ---" : "") + "\n");

            if (napaka || neObstaja || gesloNapacno) {
                n.nizNapaka(nizNapake);
            }
            n.opcije(new String[]{"u - vpišite uporabniško ime", "g - vpišite geslo", "v - spremeni vidnost gesla", "p - prijavo z zgornjimi podatki", "n - nazaj"});

            char izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 'u':
                    napaka = false;
                    gesloNapacno = false;
                    n.podnaslov("Vpišite Uporabniško Ime");
                    uporabniskoIme = n.preberiNiz();
                    neObstaja = !this.uporabniskoImeObstaja(uporabniskoIme);
                    break;
                case 'g':
                    napaka = false;
                    gesloNapacno = false;
                    n.podnaslov("Vpišite Geslo");
                    geslo = n.preberiNiz();
                    break;
                case 'p':
                    napaka = false;
                    if (neObstaja) {
                        nizNapake = "Uporabniško Ime JE NAPAČNO!";
                        break;
                    }
                    if (uporabniskoIme.equals("")) {
                        nizNapake = "*ZLOČIN* Uporabniško Ime JE PRAZNO! *ZLOČIN*";
                        neObstaja = true;
                        break;
                    }
                    if (gesloNapacno) {
                        nizNapake = "Geslo ŠE ZMER ne štima!";
                        break;
                    }
                    uporabnik = this.zbrskajUporabnika(uporabniskoIme);
                    if (uporabnik == null) {
                        nizNapake = "Imena še zmer ni najdl";
                        napaka = true;
                        break;
                    }
                    if (this.gesloPravilno(geslo, uporabnik)) {
                        prijava = false;
                        this.setTrenutniUporabnik(uporabnik);
                    } else {
                        napaka = true;
                        gesloNapacno = true;
                        nizNapake = "Pravilnost Gesla je enako je enako 0";
                    }
                    break;
                case 'v':
                    napaka = false;
                    gesloVidno = !gesloVidno;
                    break;
                case 'n':
                    return;
                default:
                    napaka = true;
                    nizNapake = "Izbrali ste NAPAČNO OPCIJO";
                    break;
            }
        }

    }

    // Ta Pošast Registrira novega uporabnika :,D
    public Uporabnik registrirajUporabnika(boolean admin) throws IOException {
        boolean napaka = false;
        String nizNapake = "";


        n.podnaslov("Vpišite *Unikatno* Uporabniško Ime");
        String uporabniskoIme = n.preberiNiz();
        n.podnaslov("Vpišite Rojeno Ime");
        String ime = n.preberiNiz();
        n.podnaslov("Prepišite Priimek iz Potnega Lista");
        String priimek = n.preberiNiz();
        boolean jeZasedeno = this.uporabniskoImeObstaja(uporabniskoIme);
        n.podnaslov("Vpišite Geslo ki ga povsod uporabljate (Geslov Ne Hashamo)");
        String geslo = n.preberiNiz();
        boolean gesloVidno = false;
        boolean uporabnikBoAdmin = false;

        boolean izbiraLoop;
        boolean registracija = true;
        while (registracija) {
            if (jeZasedeno) {
                napaka = true;
                nizNapake = "Uporabniško ime boš zih rabu zamenjat. Zih.";
            }
            n.naslov("\t\t\tRegistracija");
            System.out.println("Uporabnisko Ime: " + uporabniskoIme + ((jeZasedeno) ? " --- ZASEDENO ---" : ""));
            System.out.println("Geslo: " + ((gesloVidno) ? geslo : "*".repeat(geslo.length())));
            System.out.println("Ime in Priimek: " + ime + " " + priimek);
            if (admin) {
                System.out.println("Uporabnik Bo Admin: " + ((uporabnikBoAdmin)? "DA": "NE"));
            }

            String[] opcije = {"u - ustvari uporabniško ime", "g - ustvari geslo", "i - napiši ime", "p - napiši priimek", "r - potrditev registracije", "v - vidnost gesla", "n - nazaj"};
            if (admin) {
                opcije = new String[]{"a - admin flip-flop", "u - ustvari uporabniško ime", "g - ustvari geslo", "i - napiši ime", "p - napiši priimek", "r - potrditev registracije", "v - vidnost gesla", "n - nazaj"};
            }
            System.out.println("");
            if (napaka) {
                n.nizNapaka(nizNapake);
            }
            n.opcije(opcije);

            char izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 'a':
                    if (admin) {
                        napaka = false;
                        uporabnikBoAdmin = !uporabnikBoAdmin;
                    }
                    else {
                        napaka = true;
                        nizNapake = "Napačana opcija";
                    }
                    break;
                case 'u':
                    napaka = false;
                    izbiraLoop = true;
                    while (izbiraLoop) {
                        if (jeZasedeno) {
                            n.nizNapaka("To Uporabniško Ime Že Obstaja, Poskusi Ponovno!");
                        }
                        n.podnaslov("Vnesi Uporabnisko Ime");
                        uporabniskoIme = n.preberiNiz();
                        jeZasedeno = this.uporabniskoImeObstaja(uporabniskoIme);
                        if (!jeZasedeno && !uporabniskoIme.equals(" ")) {
                            izbiraLoop = false;
                        }
                    }
                    break;
                case 'g':
                    napaka = false;
                    izbiraLoop = true;
                    while (izbiraLoop) {
                        n.podnaslov("Vnesi Neprazno Geslo");
                        geslo = n.preberiNiz();
                        izbiraLoop = geslo.equals(" ");
                    }
                    break;
                case 'i':
                    napaka = false;
                    izbiraLoop = true;
                    while (izbiraLoop) {
                        n.podnaslov("Vpiši Od-Mati-Dano Ime");
                        ime = n.preberiNiz();
                        izbiraLoop = ime.equals(" ");
                    }
                    break;
                case 'p':
                    napaka = false;
                    izbiraLoop = true;
                    while (izbiraLoop) {
                        n.podnaslov("Prepiši Priimek s Potnega Lista");
                        priimek = n.preberiNiz();
                        izbiraLoop = priimek.equals(" ");
                    }
                    break;
                case 'v':
                    napaka = false;
                    gesloVidno = !gesloVidno;
                    break;
                case 'r':
                    if (uporabniskoIme.equals(" ") || ime.equals(" ") || priimek.equals(" ") || geslo.equals(" ") || jeZasedeno) {
                        napaka = true;
                        nizNapake = "Podatkov nisi pravilno izpolnil, verjeti ali ne!";
                        break;
                    } else {
                        Uporabnik uporabnik = new Uporabnik(ime, priimek, uporabniskoIme, geslo);
                        uporabnik.setAdmin(uporabnikBoAdmin);
                        this.dodajUporabnika(uporabnik);
                        return uporabnik;
                    }
                case 'n':
                    napaka = false;
                    registracija = false;
                    break;
                default:
                    napaka = true;
                    nizNapake = "Ta Opcija Ne Obstaja!";
            }
        }
        return null;
    }

    // Admin Toolse bom klele uštulu

    public Pocitnice adminToolsGetPocitniceFromId() throws IOException {
        n.podnaslov("Na voljo so naslednji I.D.-ji: ");
        for (Pocitnice poc : this.getSeznamPocitnic()) {
            System.out.println("Ajdi počitnic: " + poc.getIdPocitnic() + " - " + poc.getClass().getSimpleName() + " - Država: " + poc.getDrzava());
        }
        while (true) {
            n.podnaslov("Vpišite ID ali n za nazaj");
            String vnos = n.preberiNiz();
            if (vnos.equals("n")) {
                return null;
            } else {
                int iDPocitnic = 0;
                try {
                    iDPocitnic = Integer.parseInt(vnos);
                } catch (NumberFormatException e) {
                    n.podnaslov("TO NI NITI n NITI ŠTEVILKA!");
                }
                if (iDPocitnic != 0) {
                    for (Pocitnice poc : this.seznamPocitnic) {
                        if (poc.getIdPocitnic() == iDPocitnic) {
                            return poc;
                        }
                    }
                }
                n.podnaslov("NAPAČEN VNOS");
            }
        }
    }

    public void adminToolsUrediPocitnice(Pocitnice pocitnice) throws IOException {
        boolean napaka = false;
        String niznapake = "";
        while (true) {
            n.naslov("UREJANJE POCITNIC");
            pocitnice.izpisiPocSTermini(true);

            if (napaka) {
                System.out.println("");
                n.nizNapaka(niznapake);
            }
            ArrayList<String> opcije = new ArrayList<String>();
            opcije.add("i - izbriši počitnice");
            opcije.add("c - spremeni ceno");
            opcije.add( "d - spremeni državo");
            opcije.add("m - spremeni max oseb");
            opcije.add("t - uredi termine");
            opcije.add("k - končaj");

            if (pocitnice.getClass().getSimpleName().equals("Kampiranje")) {
                opcije.add(4,"s - spremeni tip kampiranja");
            }
            if (pocitnice.getClass().getSimpleName().equals("Krizarjenje")) {
                opcije.add(4, "s - spremeni kockanje");
                opcije.add(5, "o - spremeni Obiskane države");
            }
            if (pocitnice.getClass().getSimpleName().equals("Larp")) {
                opcije.add(4, "s - spremeni lastna oprema");
                opcije.add(5, "o - spremeni temo rOle playa");
            }
            if (pocitnice.getClass().getSimpleName().equals("Potovanje")) {
                opcije.add(4, "s - flip-flop bazen");
                opcije.add(5, "o - flip-flop zajtrk");
            }
            String[] opcijeArray = opcije.toArray(new String[0]);
            n.opcije(opcijeArray);
            char izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 'o':
                    if (!pocitnice.getClass().getSimpleName().equals("Kampiranje")) {
                        napaka = false;
                        pocitnice.setOpcija2(this);
                    } else {
                        napaka = true;
                        niznapake = "Ni te občine";
                    }
                    break;
                case 's':
                   napaka = false;
                   pocitnice.setOpcija1(this);
                   break;
                case 'i':
                    n.naslov("POČITNICE DELETANE! Upam da ni \"uuups\"");
                    this.removePocitnice(pocitnice);
                    return;
                case 'c':
                    napaka = false;
                    pocitnice.setCena(this);
                    break;
                case 'd':
                    napaka = false;
                    pocitnice.setDrzava(this);
                    break;
                case 'm':
                    napaka = false;
                    pocitnice.setMaxOseb(this);
                    break;
                case 't':
                    napaka = false;
                    boolean terminiLoop = true;
                    while(terminiLoop) {
                        if (pocitnice.getSeznamTerminov().size() == 0) {
                            System.out.println("");
                            n.podnaslov("NI TERMINOV!");
                            System.out.println("");
                        } else {
                            n.naslov("\t\tTERMINI: ");
                            for (Termin t : pocitnice.getSeznamTerminov()) {
                                t.izpisTermin("");
                            }
                            System.out.println("\n" + n.zvezdice + "\n");
                        }
                        if (napaka) {
                            n.nizNapaka(niznapake);
                        }
                        System.out.println("u - ustvari termin, i - izbriši termin, k - končaj");
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'u':
                                napaka = false;
                                pocitnice.ustvariTermin(this);
                                break;
                            case 'i':
                                napaka = false;
                                if (pocitnice.getSeznamTerminov().size() < 2) {
                                    napaka = true;
                                    niznapake = "Vsaj 1 termin mora vedno obstajati!";
                                } else {
                                    pocitnice.izbrisiTermine(this);
                                }
                                break;
                            case 'k':
                                napaka = false;
                                if (pocitnice.getSeznamTerminov().size() < 1) {
                                    napaka = true;
                                    niznapake = "Če si že kle, dodaj vsaj 1 termin (slej ko prej ga boš moral)";
                                }else {
                                    terminiLoop = false;
                                }
                                break;
                            default:
                                napaka = true;
                                niznapake = "Napačna Opica";
                                break;
                        }
                    }
                    break;
                case 'k':
                    if (pocitnice.getSeznamTerminov().size() == 0) {
                        napaka = true;
                        niznapake = "POČITNICE MORAJO IMETI VSAJ 1 TERMIN!!!";
                        break;
                    } else if (pocitnice.getMaxOseb() == 0 || pocitnice.getCena() == 0.0) {
                        napaka = true;
                        niznapake = "Niti cena niti število oseb ne sme biti enako 0 (oziroma 0.0 v slednjem)";
                        break;
                    }
                    return;
                default:
                    napaka = true;
                    niznapake = "Napačna Opicina";
                    break;
            }
        }
    }
    public int getIdNovihPocitnic() {
        return this.seznamPocitnic.get(this.seznamPocitnic.size() - 1).getIdPocitnic() + 1;
    }

    public void dodajUporabnika(Uporabnik u) throws FileNotFoundException {
        this.seznamUporabnikov.add(u);
        this.orm.posodobiSeznamUporabnikov(this.seznamUporabnikov);
    }

    public void resetirajVseFiltre() {
        this.filter = new Filtri();
    }

    public void setTrenutniUporabnik(Uporabnik u) {
        this.trenutniUporabnik = u;
    }

    // Getterji
    public DateTimeFormatter getFormatter() {
        return this.orm.getFormatter();
    }

    public boolean uporabniskoImeObstaja(String uporabniskoIme) {
        return this.orm.uporabniskoImeObstaja(uporabniskoIme, this.seznamUporabnikov);
    }

    public boolean gesloPravilno(String geslo, Uporabnik uporabnik) {
        if (uporabnik == null) {
            return false;
        }
        return this.orm.gesloPravilno(geslo, uporabnik);
    }

    public Uporabnik zbrskajUporabnika(String uporabniskoIme) {
        return this.orm.vrniUporabnika(uporabniskoIme, this.seznamUporabnikov);
    }

    public ArrayList<Uporabnik> getSeznamUporabnikov() {
        return this.seznamUporabnikov;
    }

    public Uporabnik getTrenutniUporabnik() {
        return this.trenutniUporabnik;
    }

    // Set Počitnice
    public void addPocitnice(Pocitnice pocitnice) throws FileNotFoundException {
        this.seznamPocitnic.add(pocitnice);
        this.posodobiPocitnice();
    }

    public void posodobiPocitnice() throws FileNotFoundException {
        this.orm.posodobiPocitnice(this.seznamPocitnic);
    }

    public void removePocitnice(Pocitnice pocitnice) throws FileNotFoundException {
        this.seznamPocitnic.remove(pocitnice);
        this.posodobiPocitnice();
    }


    // Get

    public Nizi getN() {
        return n;
    }

    public ArrayList<Pocitnice> getSeznamPocitnic() {
        return this.seznamPocitnic;
    }

    public ArrayList<Pocitnice> getFiltriranSeznam() {
        return this.filter.koncniFiltriranSeznam(this.seznamPocitnic);
    }

    public Filtri getFilter() {
        return filter;
    }

    // REZERVACIJE
    public void izpisiIzbrisiRezervacije() throws IOException {
        String vnos = "";
        boolean napaka = false;
        String nizNapake = "";
        while (true) {
            ArrayList<Rezervacija> rezervacijeUporabnika = new ArrayList<Rezervacija>();
            boolean niRezervacij = true;
            int stRezervacije = 0;
            for (Pocitnice poc : this.seznamPocitnic) {
                boolean pocitniceNeizpisane = true;
                for (Termin termin : poc.getSeznamTerminov()) {
                    boolean terminNeizpisan = true;
                    for (Rezervacija rezervacija : poc.getSeznamRezervacij()) {
                        if (this.trenutniUporabnik.getUporabniskoIme().equals(rezervacija.getUporabniskoImeUporabnika()) && termin.getIdTermina() == rezervacija.getIdTermina()) {
                            if (pocitniceNeizpisane) {
                                poc.izpisiPocitnice(this.trenutniUporabnik.getAdmin());
                                pocitniceNeizpisane = false;
                            }
                            if (terminNeizpisan) {
                                System.out.println("--------------------------------------");
                                termin.izpisTermin("");
                                terminNeizpisan = false;
                            }
                            stRezervacije += 1;
                            rezervacija.izpisiRezervacijo(stRezervacije);
                            rezervacijeUporabnika.add(rezervacija);
                            niRezervacij = false;
                        }
                    }
                }
            }
            if (niRezervacij) {
                this.n.naslov("NIMATE REZERVACIJ");
            }
            if (napaka) {
                System.out.println("");
                n.nizNapaka(nizNapake);
            }
            this.n.opcije(new String[]{"št.rezervacije - za izbris te rezervacije", "n - nazaj"});
            vnos = this.n.preberiNiz();
            if (vnos.equals("n")) {
                return;
            }

            int stIzbrisaneRez = 0;
            try {
                stIzbrisaneRez = Integer.parseInt(vnos);
            }catch (NumberFormatException ignored) {}

            if (stIzbrisaneRez > 0 && stIzbrisaneRez <= rezervacijeUporabnika.size()) {
                napaka = false;
                Rezervacija rezToRemove = rezervacijeUporabnika.get(stIzbrisaneRez - 1);
                for (Pocitnice pocitnice : this.seznamPocitnic) {
                    pocitnice.getSeznamRezervacij().remove(rezToRemove);
                }
                this.posodobiPocitnice();
            }
            else {
                napaka = true;
                nizNapake = "Ni rezervacije za dani vnos";
            }
        }
    }
}

import java.io.IOException;


public class UporabniskiVmesnik {

    public static void main(String[] args) throws IOException {
        // Pomožni klasi, Nizi za izpis stavkov, ORM za pridobivanje podatkov in pisanje v datoteke.
        final Nizi n = new Nizi();
        final TuristicnaAgencija ta = new TuristicnaAgencija();
        Uporabnik uporabnik;

        boolean napaka = false;
        String nizNapake = "";

        boolean prijavaRegistracijaUspesna = false;
        while (!prijavaRegistracijaUspesna) {
            String[] opcije = {"p - prijava", "r - registracija", "q - izhod iz programa"};


            n.naslov("Dobrodošli v Turistični Agenciji \"Nekje Je Zih Lepš\"");
            if (napaka) {
                n.nizNapaka(nizNapake);
            }
            n.opcije(opcije);

            char izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 'p':
                    ta.prijaviUporabnika();
                    if (ta.getTrenutniUporabnik() != null) {
                        prijavaRegistracijaUspesna = true;
                    }
                    break;
                case 'r':
                    uporabnik = ta.registrirajUporabnika(false);
                    if (uporabnik != null) {
                        ta.setTrenutniUporabnik(uporabnik);
                        prijavaRegistracijaUspesna = true;
                    }
                    break;
                case 'q':
                    n.naslov("Hvala, nasvidenje!");
                    return;
                default:
                    nizNapake = "Izbrali ste NAPAČNO OPCIJO";
                    napaka = true;
                    break;
            }
        }

        napaka = false;
        while (true) {

            n.naslov("\t\t\"Nekje Je Zih Lepš\"");
            System.out.println(ta.getTrenutniUporabnik().nizUporabnika());
            n.izpisiFiltre(ta);

            if (napaka) {
                n.nizNapaka(nizNapake);
            }
            String[] opcije;
            if (ta.getTrenutniUporabnik().getAdmin()) {
                opcije = new String[]{"i - izpiši Počitnice/najdi po ID", "f - filtri", "r - rezervacije", "a - admin orodja", "q - izhod iz programa"};
            } else {
                opcije = new String[]{"i - izpiši Počitnice", "f - filtri", "r - rezervacije", "q - izhod iz programa"};
            }
            n.opcije(opcije);

            char izbira = n.preberiNiz().charAt(0);
            switch (izbira) {
                case 'a':
                    if (!ta.getTrenutniUporabnik().getAdmin()) {
                        napaka = true;
                        nizNapake = "Napačna opcija (ali poskus hekanja..)";
                        break;
                    }
                    boolean adminToolsLoop = true;
                    napaka = false;
                    while (adminToolsLoop) {
                        n.naslov("ADMINISTRATORSKA ORODJA!");
                        n.opcije(new String[]{"d - dodaj počitnice", "p - uredi počitnice", "u - uredi uporabnike", "k - končaj"});

                        if (napaka) {
                            n.nizNapaka(nizNapake);
                        }
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'u':
                                napaka = false;
                                boolean urediUporabnikeLoop = true;
                                while (urediUporabnikeLoop) {
                                    n.naslov("UREJANJE UPORABNIKOV");
                                    n.opcije(new String[]{"u - ustvari novega uporabnika", "i - izbriši uporabnika", "k - končaj"});
                                    if (napaka) {
                                        n.nizNapaka(nizNapake);
                                    }
                                    izbira = n.preberiNiz().charAt(0);
                                    switch (izbira) {
                                        case 'u':
                                            napaka = false;
                                            n.naslov("Ustvarjanje Novega Uporabnika");
                                            Uporabnik noviUporabnik = ta.registrirajUporabnika(true);
                                            if (noviUporabnik != null) {
                                                n.naslov("UPORABNIK DODAM");
                                            }
                                            else {
                                                n.naslov("PREKLIC KREACIJE");
                                            }
                                            break;
                                        case 'i':
                                            napaka = false;
                                            ta.izbrisiUporabnika();
                                            break;
                                        case 'k':
                                            napaka = false;
                                            urediUporabnikeLoop = false;
                                            break;
                                        default:
                                            napaka = true;
                                            nizNapake = "Samo 3 opcije so pravilne";
                                            break;
                                    }
                                }
                                break;
                            case 'd':
                                int tipPocitnic = 0;
                                while (tipPocitnic < 1 || tipPocitnic > 4) {
                                    System.out.println("Izberi tip počitnic ki jih želiš ustvariti");
                                    System.out.println("1 - Potovanje, 2 - Larp, 3 - Križarjenje, 4 - Kampiranje");
                                    n.podnaslov("Vpiši številko (od 1 - 4)");
                                    tipPocitnic = n.preberiInt();
                                    }
                                int idPocitnic = ta.getIdNovihPocitnic();
                                Pocitnice novePocitnice = null;
                                switch (tipPocitnic) {
                                    case 1:
                                        novePocitnice = new Potovanje(idPocitnic);
                                        break;
                                    case 2:
                                        novePocitnice = new Larp(idPocitnic);
                                        break;
                                    case 3:
                                        novePocitnice = new Krizarjenje(idPocitnic);
                                        break;
                                    case 4:
                                        novePocitnice = new Kampiranje(idPocitnic);
                                        break;
                                    default:
                                        break;
                                }
                                napaka = false;
                                boolean vnosPocitnicLoop = true;
                                while(vnosPocitnicLoop) {
                                    novePocitnice.izpisiPocitnice(true);
                                    System.out.println("");
                                    if (napaka) {
                                        n.nizNapaka(nizNapake);
                                    }
                                    n.podnaslov("u - uredi "+ novePocitnice.getClass().getSimpleName() +", p - potrdi vnos, n - nazaj brez vnosa (prekliči basically)");
                                    izbira = n.preberiNiz().charAt(0);
                                    switch (izbira) {
                                        case 'u':
                                            napaka = false;
                                            ta.adminToolsUrediPocitnice(novePocitnice);
                                            break;
                                        case 'n':
                                            vnosPocitnicLoop = false;
                                            napaka = false;
                                            break;
                                        case 'p':
                                            if(novePocitnice.getSeznamTerminov().size() < 1) {
                                                napaka = true;
                                                nizNapake = "MORA BITI VSAJ 1 TERMIN!";
                                                break;
                                            }
                                            else if (novePocitnice.getCena() == 0.0) {
                                                napaka = true;
                                                nizNapake = "Dokler ne zasežemo proizvodnjih sredstev cena ne sme biti 0.";
                                                break;
                                            }
                                            else {
                                                if (novePocitnice.getMaxOseb() == 0) {
                                                    napaka = true;
                                                    nizNapake = "Maximalno število oseb ne sme biti 0.";
                                                } else {
                                                    napaka = false;
                                                    ta.addPocitnice(novePocitnice);
                                                    vnosPocitnicLoop = false;
                                                }
                                                break;
                                            }
                                        default:
                                            napaka = true;
                                            nizNapake = "Napačen vnos opcije";
                                            break;
                                    }
                                }

                                break;
                            case 'p':
                                napaka = false;
                                Pocitnice izbranePoc = ta.adminToolsGetPocitniceFromId();
                                if (izbranePoc != null) {
                                    ta.adminToolsUrediPocitnice(izbranePoc);
                                    ta.posodobiPocitnice();
                                }
                                break;
                            case 'k':
                                napaka = false;
                                adminToolsLoop = false;
                                break;
                            default:
                                napaka = true;
                                nizNapake = "Ni te opcije (admin rights under reconsideration)";
                                break;
                        }
                    }
                    break;
                case 'i':
                    napaka = false;
                    n.naslov("\t\tIzpiši Počitnice");
                    if (ta.getTrenutniUporabnik().getAdmin()) {
                        n.naslov("ADMIN ALERT!");
                        n.podnaslov("Vpišite d za iskanje počitnic po ID številki (ali karkoli drugega za izpis vseh počitnic (po filtru))");
                        izbira = n.preberiNiz().charAt(0);
                        if (izbira == 'd') {
                            Pocitnice izbranePoc = ta.adminToolsGetPocitniceFromId();
                            if (izbranePoc != null) {
                                ta.adminToolsUrediPocitnice(izbranePoc);
                            }
                        } else {
                            for (Pocitnice poc : ta.getFiltriranSeznam()) {
                                poc.izpisiPocSTermini(ta.getTrenutniUporabnik().getAdmin());
                            }
                        }
                    }
                    else {
                        for (Pocitnice poc : ta.getFiltriranSeznam()) {
                            poc.izpisiPocSTermini(ta.getTrenutniUporabnik().getAdmin());
                        }
                    }
                    break;
                case 'f':
                    napaka = false;
                    ta.getFilter().nastaviFiltre(ta);
                    break;
                case 'r':
                    napaka = false;
                    boolean rezervacija = true;
                    while (rezervacija) {
                        n.opcije(new String[]{"r - rezerviraj", "i - izpiši in/ali izbriši rezervacije", "k - končaj"});
                        if (napaka) {
                            n.nizNapaka(nizNapake);
                        }
                        izbira = n.preberiNiz().charAt(0);
                        switch (izbira) {
                            case 'r':
                                napaka = false;
                                boolean rezerviraj = true;
                                while (rezerviraj) {
                                    for (int i = 0; i < ta.getFiltriranSeznam().size(); i++) {
                                        System.out.println("");
                                        n.podnaslov("POČITNICE ŠTEVILKA: " + (i + 1));
                                        ta.getFiltriranSeznam().get(i).izpisiPocSTermini(ta.getTrenutniUporabnik().getAdmin());
                                        n.podnaslov("******");
                                    }
                                    n.izpisiFiltre(ta);
                                    n.opcije(new String[]{"št.Počitnic - rezerviraj te počitnice", "f - spremeni filter", "k - končaj"});
                                    if (napaka) {
                                        n.nizNapaka(nizNapake);
                                    }
                                    String vnos = n.preberiNiz();
                                    int stPocitnic = -1;
                                    try {
                                        stPocitnic = Integer.parseInt(vnos) - 1;
                                    } catch (NumberFormatException ignored) {
                                    }

                                    if (stPocitnic >= 0 && stPocitnic < ta.getFiltriranSeznam().size()) {
                                        ta.getFiltriranSeznam().get(stPocitnic).addRezervacija(ta);
                                    } else {
                                        switch (vnos.charAt(0)) {
                                            case 'f':
                                                napaka = false;
                                                ta.getFilter().nastaviFiltre(ta);
                                                break;
                                            case 'k':
                                                napaka = false;
                                                rezerviraj = false;
                                                break;
                                            default:
                                                napaka = true;
                                                nizNapake = "PROSIM PREVERI OPCIJE!";
                                                break;
                                        }
                                    }
                                }
                                break;
                            case 'i':
                                napaka = false;
                                ta.izpisiIzbrisiRezervacije();
                                break;
                            case 'k':
                                napaka = false;
                                rezervacija = false;
                                break;
                            default:
                                nizNapake = "\tTa Opcija \"NO GO!\"";
                                napaka = true;
                                break;
                        }
                    }
                    break;
                case 'q':
                    n.naslov("\t\tHvala In Nasvidenje!");
                    return;
                default:
                    nizNapake = "\tTa Opcija \"NO GO!\"";
                    napaka = true;
                    break;
            }
        }

    }

}
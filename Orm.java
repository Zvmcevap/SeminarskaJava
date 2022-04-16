import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Orm {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    // Uporabniki
    FileReader usr = new FileReader("uporabniki.txt");
    BufferedReader uporBralec = new BufferedReader(usr);

    // Pačitnce
    FileReader poc = new FileReader("pocitnice.txt");
    BufferedReader pocBralec = new BufferedReader(poc);

    // Termini
    FileReader ter = new FileReader("termini.txt");
    BufferedReader terBralec = new BufferedReader(ter);

    // Rezervacije
    FileReader rez = new FileReader("rezervacije.txt");
    BufferedReader rezBralec = new BufferedReader(rez);

    public Orm() throws FileNotFoundException {
    }

    // Metode za Uporabnike

    public ArrayList<Uporabnik> getSeznamUporabnikov() throws IOException {
        ArrayList<Uporabnik> seznamUporabnikov = new ArrayList<Uporabnik>();
        ArrayList<String> niziUporabnikov = new ArrayList<String>();
        Uporabnik uporabnik;

        while (uporBralec.ready()) {
            niziUporabnikov.add(uporBralec.readLine());
        }
        uporBralec.close();

        for (String niz : niziUporabnikov) {
            String[] podatki = niz.split(",");
            uporabnik = new Uporabnik(podatki[0], podatki[1], podatki[2], podatki[3]);
            boolean admin = Objects.equals(podatki[4], "true");
            uporabnik.setAdmin(admin);
            seznamUporabnikov.add(uporabnik);
        }

        return seznamUporabnikov;
    }

    public void posodobiSeznamUporabnikov(ArrayList<Uporabnik> seznam) throws FileNotFoundException {

        PrintWriter pwd = new PrintWriter("uporabniki.txt");
        for (Uporabnik user : seznam) {
            pwd.println(user.getIme() + "," + user.getPriimek() + "," + user.getUporabniskoIme() + "," + user.getGeslo() + "," + user.getAdmin());
        }
        pwd.close();
    }

    public boolean uporabniskoImeObstaja(String ime, ArrayList<Uporabnik> seznamUporabnikov) {
        for (Uporabnik uporabnik : seznamUporabnikov) {
            if (ime.equals(uporabnik.getUporabniskoIme())) {
                return true;
            }
        }
        return false;
    }

    public Uporabnik vrniUporabnika(String ime, ArrayList<Uporabnik> seznamUporabnikov) {
        for (Uporabnik u : seznamUporabnikov) {
            if (ime.equals(u.getUporabniskoIme())) {
                return u;
            }
        }
        return null;
    }

    public boolean gesloPravilno(String geslo, Uporabnik uporabnik) {
        return geslo.equals(uporabnik.getGeslo());
    }

    // Metode Za Počitnice

    public ArrayList<Pocitnice> getSeznamPocitnic() throws IOException {
        ArrayList<Pocitnice> seznamPocitnic = new ArrayList<Pocitnice>();
        ArrayList<String> niziPocitnic = new ArrayList<String>();

        while (pocBralec.ready()) {
            niziPocitnic.add(pocBralec.readLine());
        }
        pocBralec.close();
        for (String niz : niziPocitnic) {
            switch (niz.charAt(0)) {
                case '0':
                    seznamPocitnic.add(getPotovanjeIzNiza(niz));
                    break;
                case '1':
                    seznamPocitnic.add(getLarpIzNiza(niz));
                    break;
                case '2':
                    seznamPocitnic.add(getKrizarjenjeIzNiza(niz));
                    break;
                case '3':
                    seznamPocitnic.add(getKampiranjeIzNiza(niz));
                    break;
                default:
                    System.out.println("Burn The PC!");
                    break;
            }
        }
        dodajTerminePocitnicam(seznamPocitnic);
        dodajRezervacijePocitnicam(seznamPocitnic);

        return seznamPocitnic;
    }

    public Potovanje getPotovanjeIzNiza(String niz) {
        String[] podatki = niz.split(",");
        int id = Integer.parseInt(podatki[1]);
        String drzava = podatki[2];
        double cena = Double.parseDouble(podatki[3]);
        int maxOseb = Integer.parseInt(podatki[4]);
        boolean bazen = Boolean.parseBoolean(podatki[5]);
        boolean zajtrk = Boolean.parseBoolean(podatki[6]);

        return new Potovanje(id, drzava, cena, maxOseb, bazen, zajtrk);
    }

    public Larp getLarpIzNiza(String niz) {
        String[] podatki = niz.split(",");
        int id = Integer.parseInt(podatki[1]);
        String drzava = podatki[2];
        double cena = Double.parseDouble(podatki[3]);
        int maxOseb = Integer.parseInt(podatki[4]);
        String tematika = podatki[5];
        boolean lastnaOprema = Boolean.parseBoolean(podatki[6]);

        return new Larp(id, drzava, cena, maxOseb, tematika, lastnaOprema);
    }

    public Krizarjenje getKrizarjenjeIzNiza(String niz) {
        String[] podatki = niz.split(",");
        int id = Integer.parseInt(podatki[1]);
        String drzava = podatki[2];
        double cena = Double.parseDouble(podatki[3]);
        int maxOseb = Integer.parseInt(podatki[4]);
        String[] drzaveArray = podatki[5].split(":");
        ArrayList<String> obiskaneDrzave = new ArrayList<String>(List.of(drzaveArray));
        boolean kockanje = Boolean.parseBoolean(podatki[6]);

        return new Krizarjenje(id, drzava, cena, maxOseb, obiskaneDrzave, kockanje);
    }

    public Kampiranje getKampiranjeIzNiza(String niz) {
        String[] podatki = niz.split(",");
        int id = Integer.parseInt(podatki[1]);
        String drzava = podatki[2];
        double cena = Double.parseDouble(podatki[3]);
        int maxOseb = Integer.parseInt(podatki[4]);
        int tipKamp = Integer.parseInt(podatki[5]);

        return new Kampiranje(id, drzava, cena, maxOseb, tipKamp);
    }

    public void dodajTerminePocitnicam(ArrayList<Pocitnice> seznamPocitnic) throws IOException {
        ArrayList<String> niziTerminov = new ArrayList<String>();

        while (terBralec.ready()) {
            niziTerminov.add(terBralec.readLine());
        }
        terBralec.close();
        for (String niz : niziTerminov) {
            String[] podatki = niz.split(",");
            int idPocitnic = Integer.parseInt(podatki[0]);
            int idTermina = Integer.parseInt(podatki[1]);
            String odhod = podatki[2];
            String prihod = podatki[3];
            for (Pocitnice poc : seznamPocitnic) {
                if (poc.getIdPocitnic() == idPocitnic) {
                    poc.addTermin(new Termin(idTermina, odhod, prihod));
                }
            }
        }
    }

    public void dodajRezervacijePocitnicam(ArrayList<Pocitnice> seznamPocitnic) throws IOException {
        ArrayList<String> niziRezervacij = new ArrayList<String>();

        while (rezBralec.ready()) {
            niziRezervacij.add(rezBralec.readLine());
        }
        rezBralec.close();

        for (String niz : niziRezervacij) {
            String[] podatki = niz.split(",");
            int idPocitnic = Integer.parseInt(podatki[0]);
            int idTermina = Integer.parseInt(podatki[1]);
            String uporIme = podatki[2];
            String ime = podatki[3];
            String priimek = podatki[4];
            int stOdraslih = Integer.parseInt(podatki[5]);
            int stOtrok = Integer.parseInt(podatki[6]);

            for(Pocitnice poc : seznamPocitnic) {
                if(poc.getIdPocitnic() == idPocitnic) {
                    poc.getSeznamRezervacij().add(new Rezervacija(idTermina, uporIme, ime, priimek, stOdraslih, stOtrok));
                }
            }
        }
    }

    public void posodobiPocitnice(ArrayList<Pocitnice> seznamPocitnic) throws FileNotFoundException {
        PrintWriter pwt = new PrintWriter("termini.txt");
        PrintWriter pwr = new PrintWriter("rezervacije.txt");
        PrintWriter pwp = new PrintWriter("pocitnice.txt");
        for (Pocitnice poc : seznamPocitnic) {
            for (Termin t : poc.getSeznamTerminov()) {
                pwt.println(poc.getIdPocitnic() + "," + t.getOrmNiz());
            }
            for (Rezervacija r : poc.getSeznamRezervacij()) {
                pwr.println(poc.getIdPocitnic() + "," + r.getOrmNiz());
            }
            pwp.println(poc.getOrmNiz());
        }
        pwt.close();
        pwr.close();
        pwp.close();
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
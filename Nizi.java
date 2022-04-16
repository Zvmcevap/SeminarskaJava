import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Nizi {

    final InputStreamReader isr = new InputStreamReader(System.in);
    final BufferedReader br = new BufferedReader(isr);

    String zvezdice = "******************************************************";
    String zacetneZvezdice = "******\t";
    String koncneZvezdice = "\t******";

    public void naslov(String n) {
        System.out.println("\n" + zvezdice);
        System.out.println(n);
        System.out.println(zvezdice + "\n");
    }

    public void podnaslov(String n) {
        System.out.println(zacetneZvezdice + n + koncneZvezdice);
    }

    public void opcije(String[] opcije) {
        System.out.println("");
        System.out.println(zacetneZvezdice + "Na izbiro so naslednje opcije" + koncneZvezdice);

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < opcije.length; i++) {
            if (i % 3 == 0 && i != 0) {
                temp.append("\n");
            }
            temp.append(opcije[i]);
            temp.append("\t");
        }
        System.out.println(temp);
        System.out.println("Vpišite črko: ");
    }

    public void nizNapaka(String tekstNapake) {
        System.out.println(zacetneZvezdice + tekstNapake + koncneZvezdice);
    }

    public String preberiNiz() throws IOException {

        String vnos = br.readLine().trim();
        vnos = vnos.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", "");
        if (vnos.equals("")) {
            vnos = " ";
        }
        return vnos;
    }

    public int preberiInt() throws IOException {
        String vnos = br.readLine().trim();
        int stVnosa = 0;
        try {
            stVnosa = Integer.parseInt(vnos);
        } catch (NumberFormatException ignored) {
        }
        return stVnosa;
    }
    public double preberiDouble() throws IOException {
        String vnos = br.readLine().trim();
        double stVnosa = 0.0;
        try {
            stVnosa = Double.parseDouble(vnos);
        } catch (NumberFormatException ignored) {
        }
        return stVnosa;
    }

    public String dobiDatum(boolean inUro) throws IOException {
        String datum = "";
        int[] maliMeseci = new int[]{4, 6, 9, 11};
        int mesec = 0;
        int dan = 0;
        int leto = 0;

        // BREZ URE
        while (mesec < 1 || mesec > 12) {
            this.podnaslov("Vpiši Mesec (1 - 12)");
            mesec = this.preberiInt();
        }
        while (dan < 1 || dan > 31) {
            this.podnaslov("Vpiši dan (1 - 31): ");
            dan = preberiInt();
        }
        while (2000 > leto || leto > 2100) {
            this.podnaslov("Vpiši leto (med 2000 - 2100 sem se odločil da bo meja)");
            leto = preberiInt();
        }
        if (mesec == 2 && dan > 28) {
            dan = 28;
        }
        if (dan == 31) {
            for (int m : maliMeseci) {
                if (mesec == m) {
                    dan = 30;
                    break;
                }
            }
        }
        // URA
        String uraNiz = "00:00";
        if (inUro) {
            podnaslov("Vpiši uro (0-23)");
            int ura = preberiInt();
            if (ura > 23) {
                ura = 23;
            }
            if (ura < 0) {
                ura = 0;
            }
            podnaslov("Vpiši minute (0-59)");
            int minuta = preberiInt();
            if (minuta > 59) {
                minuta = 59;
            }
            if (minuta < 0) {
                minuta = 0;
            }
            String hour = (ura < 10) ? "0" + ura : "" + ura;
            String minute = (minuta < 10) ? ":0" + minuta : ":" + minuta;
            uraNiz = hour + minute;
        }
        String danNiz = (dan > 9) ? String.valueOf(dan) : "0" + dan;
        String mesecNiz = (mesec > 9) ? String.valueOf(mesec) : "0" + mesec;
        datum = danNiz + "." + mesecNiz + "." + leto + " " + uraNiz;

        return datum;
    }

    public void izpisiFiltre(TuristicnaAgencija ta) {
        String nizOdhod = "Datum od: " + ((ta.getFilter().getOdhod() == null) ? "NI DOLOČEN" : ta.getFilter().getOdhod().format(ta.getFormatter()));
        String nizPrihod = " - do: " + ((ta.getFilter().getPrihod() == null) ? "NI DOLOČEN" : ta.getFilter().getPrihod().format(ta.getFormatter()));
        String nizTipografija = "Tipi Počitnic: " + (ta.getFilter().isFiltrirajPodrazred() ? ta.getFilter().getNizTipi() : "VSI");

        System.out.println("");
        this.podnaslov("FILTRI");
        System.out.println(nizOdhod + nizPrihod);
        System.out.println(ta.getFilter().getNizCena());
        System.out.println(ta.getFilter().getNizDrzave());
        System.out.println(nizTipografija);
        System.out.println("-".repeat(20));
        System.out.println("Sortirano po: " + ta.getFilter().getSortiranjePo() + (ta.getFilter().isPadajoce()? " - PADAJOČE":" - VZHAJAJOČE"));
    }

}
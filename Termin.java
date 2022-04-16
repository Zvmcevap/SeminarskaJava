import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Termin {

	private LocalDateTime odhod;
	private LocalDateTime prihod;
	private final int idTermina;
	
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	
	public Termin(int idTermina, String odhod, String prihod) {

		this.idTermina = idTermina;
		this.odhod = LocalDateTime.parse(odhod, formatter);
		this.prihod = LocalDateTime.parse(prihod, formatter);

	}
	
	// Setrji
	
	public void setPrihod(String prihod) {
		this.prihod = LocalDateTime.parse(prihod, formatter);
	}
	
	public void setOdhod(String odhod) {
		this.odhod = LocalDateTime.parse(odhod, formatter);
	}
	
	// Go-getterji
	public int getZasedenostOdrasli(ArrayList<Rezervacija> seznamRezervacij) {
		int zasedenost = 0;
		for (Rezervacija rez : seznamRezervacij) {
			if (rez.getIdTermina() == this.idTermina) {
				zasedenost += rez.getSteviloOdraslih();
			}
		}
		return zasedenost;
	}
	public int getZasedenostOtroci(ArrayList<Rezervacija> seznamRezervacij) {
		int zasedenost = 0;
		for (Rezervacija rez : seznamRezervacij) {
			if (rez.getIdTermina() == this.idTermina) {
				zasedenost += rez.getSteviloOtrok();
			}
		}
		return zasedenost;
	}

	public int getIdTermina() {
		return idTermina;
	}

	public LocalDateTime getOdhod(){
		return this.odhod;
	}
	
	public LocalDateTime getPrihod(){
		return this.prihod;
	}
	
	public String nizTermina(){
		return "Termin "+ this.idTermina + ": Odhod: " + this.odhod.format(formatter) + " - prihod: " + this.prihod.format(formatter);
	}

	public String getOrmNiz() {
		return "" + this.idTermina + "," + this.odhod.format(formatter) + "," + this.prihod.format(formatter);
	}
	// Izpis
	public void izpisTermin(String adminNiz){
		System.out.println(this.nizTermina() + " " + adminNiz);
	}

}

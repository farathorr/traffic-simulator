package simu.model;

import simu.framework.*;

// TODO:
// Customer koodataan simulointimallin edellyttämällä tavalla (data!)
public class Customer {
	private double saapumisaika;
	private double poistumisaika;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	
	public Customer() {
	    id = i++;
	    
		saapumisaika = Clock.getInstance().getTime();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika);
	}

	public double getPoistumisaika() {
		return poistumisaika;
	}

	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	public double getSaapumisaika() {
		return saapumisaika;
	}

	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	


	public int getId() {
		return id;
	}
	
	public void report() {
		Trace.out(Trace.Level.INFO, "\nCustomer "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Customer "+id+ " saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " viipyi: " +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}

}

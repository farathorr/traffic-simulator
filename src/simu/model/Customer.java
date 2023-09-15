package simu.model;

import simu.framework.*;

// TODO:
// Customer koodataan simulointimallin edellyttämällä tavalla (data!)
public class Customer {
	private double arrivalTime;
	private double leavingTime;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	
	public Customer() {
	    id = i++;
	    
		arrivalTime = Clock.getInstance().getTime();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+ arrivalTime);
	}

	public double getLeavingTime() {
		return leavingTime;
	}

	public void setLeavingTime(double leavingTime) {
		this.leavingTime = leavingTime;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	


	public int getId() {
		return id;
	}
	
	public void report() {
		Trace.out(Trace.Level.INFO, "\nCustomer "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Customer "+id+ " saapui: " + arrivalTime);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " poistui: " + leavingTime);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " viipyi: " +(leavingTime - arrivalTime));
		sum += (leavingTime - arrivalTime);
		double average = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ average);
	}

}

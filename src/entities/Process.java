package entities;
public class Process {
	int PID;
	int arrivalTime;
	int serviceTime;
	int starttijd;
	int eindtijd;
	int wachttijd;
	double TAT;
	double nTAT;
	int remainingTime;
	
	
	public Process(){
		PID=0;
		arrivalTime=0;
		serviceTime=0;
		starttijd=0;
		eindtijd=0;
		wachttijd=0;
		TAT=0;
		nTAT=0;
		remainingTime=0;
	}
	
	public Process(int pID, int arrivalTime, int serviceTime, int starttijd, int eindtijd, int wachttijd, double tAT,
			double nTAT, int remainingTime) {
		this.PID = pID;
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
		this.starttijd = starttijd;
		this.eindtijd = eindtijd;
		this.wachttijd = wachttijd;
		this.TAT = tAT;
		this.nTAT = nTAT;
		this.remainingTime=remainingTime;
	}
	
	public Process(Process p) {
		this.PID = p.getPID();
		this.arrivalTime = p.getArrivalTime();
		this.serviceTime = p.getServiceTime();
		this.starttijd = p.getStarttijd();
		this.eindtijd = p.getEindtijd();
		this.wachttijd = p.getWachttijd();
		this.TAT = p.getTAT();
		this.nTAT = p.getnTAT();
		this.remainingTime=p.getRemainingTime();
	}
	
	public Process(int pID, int arrivalTime, int serviceTime) {
		this.PID = pID;
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
		this.remainingTime= serviceTime;
	}

	
	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getStarttijd() {
		return starttijd;
	}

	public void setStarttijd(int starttijd) {
		this.starttijd = starttijd;
	}

	public int getEindtijd() {
		return eindtijd;
	}

	public void setEindtijd(int eindtijd) {
		this.eindtijd = eindtijd;
	}

	public int getWachttijd() {
		return wachttijd;
	}

	public void setWachttijd(int wachttijd) {
		this.wachttijd = wachttijd;
	}

	public double getTAT() {
		return TAT;
	}

	public void setTAT(double tAT) {
		TAT = tAT;
	}

	public double getnTAT() {
		return nTAT;
	}

	public void setnTAT(double nTAT) {
		this.nTAT = nTAT;
	}


}

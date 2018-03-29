package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import entities.Process;

public class Algoritmes {
	ArrayList<Process> processen=new ArrayList<>();
	public Algoritmes() {
		
	}
	
	public ArrayList<Process> getProcessen() {
		return processen;
	}
	
	
	// effectieve algoritmes
	public ArrayList<Process> bewerkProcessenFCFS(ArrayList<Process> processen) {
		int huidig = 0;

		// sorteren op arrival time (First Come First Serve)
		Collections.sort(processen, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});

		// voor elk processen tijden instellen
		for (Process p : processen) {

			// proces is aangekomen tijdens uitvoering van een ander proces
			if (huidig > p.getArrivalTime()) {
				p.setStarttijd(huidig);
			} else {
				p.setStarttijd(p.getArrivalTime());
			}

			p.setEindtijd(p.getStarttijd() + p.getServiceTime());

			huidig = p.getEindtijd();

			p.setWachttijd(p.getStarttijd() - p.getArrivalTime());

			p.setTAT(p.getWachttijd() + p.getServiceTime());

			p.setnTAT(p.getTAT() / p.getServiceTime());

		}
		return processen;

	}
	
	public ArrayList<Process> bewerkProcessenSJF(ArrayList<Process> processen) {
		
		/*	
		 * kopie van binnenkomende processen aangezien de processen een voor een verwijderd worden
		 * 	en de binnenkomende lijst nog gebruikt wordt voor de andere algoritmes
		 */
		LinkedList<Process> procesLijst= new LinkedList<Process>(processen);
		
		
		// processen die uiteindelijk gereturned zullen worden
		ArrayList<Process> bewerkteProcessen = new ArrayList<Process>();
		
		// bijhouden van aangekomen processen
		LinkedList<Process> aangekomen = new LinkedList<Process>();

		// binnenkomende processen sorteren op aankomsttijd
		Collections.sort(procesLijst, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});

		// eerste proces toevoegen aan aangekomen aangezien dit het eerste proces is doen we het hier nog buiten de lus
		aangekomen.add(procesLijst.get(0));
		
		// verwijderen uit processen aangezien we deze dus niet meer zullen in rekening brengen
		procesLijst.remove(0);
		
		// proces opvragen en eindtijd instellen
		aangekomen.get(0).setStarttijd(aangekomen.get(0).getArrivalTime());
		aangekomen.get(0).setEindtijd(aangekomen.get(0).getArrivalTime() + aangekomen.get(0).getServiceTime());
		aangekomen.get(0).setWachttijd(aangekomen.get(0).getEindtijd()-aangekomen.get(0).getArrivalTime()-aangekomen.get(0).getServiceTime());
		aangekomen.get(0).setTAT(aangekomen.get(0).getEindtijd()-aangekomen.get(0).getArrivalTime());
		aangekomen.get(0).setnTAT(aangekomen.get(0).getTAT()/aangekomen.get(0).getServiceTime());
		
		
		// klok start bij het eind van het eerste proces
		int huidigeTijd = aangekomen.get(0).getEindtijd();
		bewerkteProcessen.add(aangekomen.get(0));
		aangekomen.remove(0);
		
		
		// zolang er nog processen zijn en er processen zijn aangekomen, voer uit...
		while (procesLijst.size() != 0 || aangekomen.size() != 0) {
			
			
			// nieuwe processen zoeken die zijn aangekomen en toevoegen aan de aangekomen lijst tijdens uitvoering
			int a=0;
			while ( a<procesLijst.size()-1 && huidigeTijd >= procesLijst.get(a).getArrivalTime() ) {
				
				aangekomen.add(procesLijst.get(a));
				procesLijst.remove(procesLijst.get(a));
				a++;
				
				
			}
			
			
			// indien er geen processen meer aangekomen voor de huidige tijd, eerste proces uit processen opvragen en huidigeTijd shiften naar begin van dit proces
			if (aangekomen.size() == 0) {
				aangekomen.add(procesLijst.get(0));
				huidigeTijd = procesLijst.get(0).getArrivalTime();
				procesLijst.remove(procesLijst.get(0));
			}

			

			
					
			// aangekomen processen sorteren op service time 
			Collections.sort(aangekomen, new Comparator<Process>() {

				public int compare(Process s1, Process s2) {
					return Integer.compare(s1.getServiceTime(), s2.getServiceTime());
				}
			});
			
			// kortste process zoeken
			Process shortestProcess = aangekomen.get(0);
			// uit aangekomen verwijderen aangezien we dit proces inplannen en dus later niet meer zullen beschouwen
			aangekomen.remove(shortestProcess);
			// starttijd van kortste proces instellen
			shortestProcess.setStarttijd(huidigeTijd);

			// huidigeTijd updaten
			huidigeTijd = huidigeTijd + shortestProcess.getServiceTime();
			
			// waarden van proces aanpassen
			shortestProcess.setEindtijd(huidigeTijd);
			shortestProcess.setWachttijd(shortestProcess.getStarttijd()-shortestProcess.getArrivalTime());
			shortestProcess.setTAT(shortestProcess.getWachttijd()+shortestProcess.getServiceTime());
			shortestProcess.setnTAT(shortestProcess.getTAT()/shortestProcess.getServiceTime());
			
			
			bewerkteProcessen.add(shortestProcess);
		}
		return bewerkteProcessen;

	}
	public ArrayList<Process> bewerkProcessenMLFB(ArrayList<Process> processen, int [] lengtes) {
		
		// we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		
		LinkedList<Process> procesLijst= new LinkedList<Process>(processen);
		for (Process x: procesLijst) {
			x.setEindtijd(0);
			x.setWachttijd(0);
			x.setRemainingTime(x.getServiceTime());
			x.setnTAT(0);
			x.setTAT(0);
		}
		
		// binnenkomende processen sorteren op aankomsttijd
		Collections.sort(procesLijst, new Comparator<Process>() {
			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});
		
		
		
		// init van bij te houden tijd en huidige proces
		int huidigeTijd;
		Process p;
		
		// te returnen list
		ArrayList<Process> bewerkteProcessen=new ArrayList<>();
		
		
		int q1=lengtes[0];
		int q2=lengtes[1];
		int q3=lengtes[2];
		int huidigeSlice;
		
		
		// queues initializeren
		LinkedList<Process> processqueue1=new LinkedList<>();
		LinkedList<Process> processqueue2=new LinkedList<>();
		LinkedList<Process> processqueue3=new LinkedList<>();
		LinkedList<Process> volgendeQueue;
		
		
		// initializeren van algoritme
		huidigeTijd = procesLijst.get(0).getArrivalTime();
		//toevoegen aan eerste queue
        processqueue1.addLast(procesLijst.remove(0));
        
        
        
        /////////// KEUZE PROCES P ///////////////
        
        // zolang er nog processen aanwezig zijn, voer uit        
        while (!processqueue1.isEmpty() || !processqueue2.isEmpty() || !processqueue3.isEmpty() || !procesLijst.isEmpty()) {
        	
            //Volgende process selecteren
            if (!processqueue1.isEmpty()) {
                p = processqueue1.removeFirst();
                huidigeSlice = q1;
                volgendeQueue = processqueue2;           
            } 
            
            else if (!processqueue2.isEmpty()) {
                p = processqueue2.removeFirst();
                huidigeSlice = q2;
                volgendeQueue = processqueue3;
            } 
            
            else if (!processqueue3.isEmpty()) {
                p = processqueue3.removeFirst();
                huidigeSlice = q3;
                volgendeQueue = processqueue3;
            } 
            
            else  {
        	// als er geen processen meer in de queues zitten, haal een uit binnenkomende processen
            if (!procesLijst.isEmpty()) {
            	
                p = procesLijst.remove(0);
                huidigeTijd = p.getArrivalTime();
               
               
                volgendeQueue = processqueue2;
                huidigeSlice = q1;
            } 
            else break;
            }
           
            
            p.setStarttijd(huidigeTijd); 
            
            
            //gekozen proces kan uitvoeren totdat timeslice eindigt of het proces eindigt
            if (p.getRemainingTime() <= huidigeSlice) { //process eindigt
            	
                huidigeTijd += p.getRemainingTime();
                
                p.setEindtijd(huidigeTijd);
               
                p.setWachttijd(p.getEindtijd()-p.getServiceTime()-p.getArrivalTime());
             
                p.setTAT(p.getEindtijd()-p.getArrivalTime());
                p.setnTAT(p.getTAT()/p.getServiceTime());
                bewerkteProcessen.add(p);
             
            } else {
            	
                huidigeTijd += huidigeSlice;
                p.setEindtijd(huidigeTijd);
                
                
                p.setRemainingTime(p.getRemainingTime() - huidigeSlice);
                volgendeQueue.addLast(p);
            }

           
           
            
            int a=0;
			while ( a<procesLijst.size()-1 && huidigeTijd >= procesLijst.get(a).getArrivalTime() ) {
				
				processqueue1.add(procesLijst.get(a));
				procesLijst.remove(procesLijst.get(a));
				
				
				a++;
				
				
			}
            
            
            
            //currentProcess = null;

        }

        
        
        return bewerkteProcessen;
        
        
		
		
		
		
	}
	public ArrayList<Process> bewerkProcessenRRQ2(ArrayList<Process> processen){
		
		//0.1) we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
		for (Process x: procesLijst) {
			x.setEindtijd(0);
			x.setWachttijd(0);
			x.setRemainingTime(x.getServiceTime());
			x.setnTAT(0);
			x.setTAT(0);
		}
		
		//0.2) procesLijst sorteren op arrivalTime
		Collections.sort(procesLijst, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});
		
		
		
		
		//1) definiëren van de variabelen
		
			//1.1) de lijsten die we zullen gebruiken
			ArrayList<Process> klaarLijst = new ArrayList<Process>();
			LinkedList<Process> wachtLijst = new LinkedList<Process>();
			
			//1.2) totale aantal processen
			int totAantalProcessen = processen.size();
			
			//1.3) totaal aantalk processen al genomen
			int genomenProcessen = 0;
			
			//1.4) de maximale tijd die een proces kan uitgevoerd worden vooraleer we switchen naar een ander process
			int qTime = 2;
			
			//1.5) tijdelijk process, zodat we niet telkens een nieuw moeten aanmaken
			Process pTemp;
			
			//1.6) huidige tijd op de tijd waarin het eerste process zal binnekomen zetten
			int huidigeTijd= procesLijst.get(0).getArrivalTime();
			
			//1.7) remaining time variabele (kan hier enkel 1 zijn, in andere geval 1.2.3.4.5.6.7
			int remainingTime; //hoogst waarschijnlijk niet nodig
			
		
		
		
			
			//System.out.println("size"+procesLijst.size());
		//2) executen van de algoritme
			//zolang niet alle processen executed zijn
			while(klaarLijst.size() != totAantalProcessen) {
				
				
				//2.1) kijken als er voor het huidige tijdstip nog processen moeten binnekomen	
				while ( !procesLijst.isEmpty() &&huidigeTijd >= procesLijst.get(0).getArrivalTime() ) {
					
					wachtLijst.addLast(procesLijst.get(0));
					procesLijst.remove(0);
					genomenProcessen++;
					
					
				}
				
				//2.2) logica voor in de wachtlijst: 
					if(wachtLijst.isEmpty()) {
						huidigeTijd = procesLijst.get(0).getArrivalTime();
						//ook mog om huidige tijd te zetten op de arrivaltime van het volgende process
					}
					
					//als de wachtlijst niet leeg is
					else {
						//pak het eerste proces in de wachttijd
						pTemp = wachtLijst.removeFirst();
						
						//als remainingtime = servicetime, dan is het de 1e keer dat het wordt uitgevoerd, dus starttijd is dan
						if(pTemp.getRemainingTime()==pTemp.getServiceTime()) {pTemp.setStarttijd(huidigeTijd);}
					
			 			//als de remaining service time nog minder dan 2 is
						if(pTemp.getRemainingTime()<qTime) {
							
							huidigeTijd = huidigeTijd + pTemp.getRemainingTime();
							pTemp.setRemainingTime(0);
							
							//process is dan klaar 
							pTemp.setEindtijd(huidigeTijd);
							klaarLijst.add(pTemp);
						}
						
						//als de remaining service time =2
						else if(pTemp.getRemainingTime()==qTime){
							
							
							 huidigeTijd = huidigeTijd + qTime;
							 pTemp.setRemainingTime(0);
							 
							 //process is klaar, voeg het toe aan de klaarlijst
							 pTemp.setEindtijd(huidigeTijd);
							 klaarLijst.add(pTemp);
						}
						
						//als de remainnig service time > 2 
						else {
							pTemp.setRemainingTime(pTemp.getRemainingTime()-qTime);
							huidigeTijd = huidigeTijd +qTime;
							
							//process kan niet klaar zijn
							//voeg het achteraan toe in de wachtlijst
							wachtLijst.addLast(pTemp);
						}
					//System.out.println("geraakt nietuit een lege wachtlijst");
					//einde van als de wachtlijst niet leeg is	
					}
				//System.out.println("geraakt aan 543");
				//System.out.println("size" + klaarLijst.size());
				//System.out.println("aantal elementen in wachtLijst" + wachtLijst.size());
				//einde van de while niet alle processen in klaar zitten
				}
			
			
			
			
			
		//System.out.println("geraakt aan 549");	
		//3) adhv de eindtijden kunnen we de rest telkens berekenen
		for(Process p : klaarLijst) {
			p.setWachttijd( p.getEindtijd() - p.getArrivalTime() - p.getServiceTime() );
			
			p.setTAT( p.getEindtijd() - p.getArrivalTime() );
			
			p.setnTAT( p.getTAT()/p.getServiceTime()  );
		}
		
	//System.out.println("geraakt hier");
	return klaarLijst;
	//einde methode bewerkprocessenRRQ2	
	}
	public ArrayList<Process> bewerkProcessenRRQ8(ArrayList<Process> processen){
		
		//0.1) we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
		for (Process x: procesLijst) {
			x.setEindtijd(0);
			x.setWachttijd(0);
			x.setRemainingTime(x.getServiceTime());
			x.setnTAT(0);
			x.setTAT(0);
		}
		
		//0.2) procesLijst sorteren op arrivalTime
		Collections.sort(procesLijst, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});
		
		
		
		
		//1) definiëren van de variabelen
		
			//1.1) de lijsten die we zullen gebruiken
			ArrayList<Process> klaarLijst = new ArrayList<Process>();
			LinkedList<Process> wachtLijst = new LinkedList<Process>();
			
			//1.2) totale aantal processen
			int totAantalProcessen = processen.size();
			
			//1.3) totaal aantalk processen al genomen
			int genomenProcessen = 0;
			
			//1.4) de maximale tijd die een proces kan uitgevoerd worden vooraleer we switchen naar een ander process
			int qTime = 8;
			
			//1.5) tijdelijk process, zodat we niet telkens een nieuw moeten aanmaken
			Process pTemp;
			
			//1.6) huidige tijd op de tijd waarin het eerste process zal binnekomen zetten
			int huidigeTijd= procesLijst.get(0).getArrivalTime();
			
			//1.7) remaining time variabele (kan hier enkel 1 zijn, in andere geval 1.2.3.4.5.6.7
			int remainingTime; //hoogst waarschijnlijk niet nodig
			
		
		
		
			
			//System.out.println("size"+procesLijst.size());
		//2) executen van de algoritme
			//zolang niet alle processen executed zijn
			while(klaarLijst.size() != totAantalProcessen) {
				
				
				//2.1) kijken als er voor het huidige tijdstip nog processen moeten binnekomen	
				while ( !procesLijst.isEmpty() &&huidigeTijd >= procesLijst.get(0).getArrivalTime() ) {
					
					wachtLijst.addLast(procesLijst.get(0));
					procesLijst.remove(0);
					genomenProcessen++;
					
					
				}
				
				//2.2) logica voor in de wachtlijst: 
					if(wachtLijst.isEmpty()) {
						huidigeTijd = procesLijst.get(0).getArrivalTime();
						//ook mog om huidige tijd te zetten op de arrivaltime van het volgende process
					}
					
					//als de wachtlijst niet leeg is
					else {
						//pak het eerste proces in de wachttijd
						pTemp = wachtLijst.removeFirst();
						
						//als remainingtime = servicetime, dan is het de 1e keer dat het wordt uitgevoerd, dus starttijd is dan
						if(pTemp.getRemainingTime()==pTemp.getServiceTime()) {pTemp.setStarttijd(huidigeTijd);}
					
			 			//als de remaining service time nog minder dan 8 is
						if(pTemp.getRemainingTime()<qTime) {
							
							huidigeTijd = huidigeTijd + pTemp.getRemainingTime();
							pTemp.setRemainingTime(0);
							
							//process is dan klaar 
							pTemp.setEindtijd(huidigeTijd);
							klaarLijst.add(pTemp);
						}
						
						//als de remaining service time =8
						else if(pTemp.getRemainingTime()==qTime){
							
							
							 huidigeTijd = huidigeTijd + qTime;
							 pTemp.setRemainingTime(0);
							 
							 //process is klaar, voeg het toe aan de klaarlijst
							 pTemp.setEindtijd(huidigeTijd);
							 klaarLijst.add(pTemp);
						}
						
						//als de remainnig service time > 8
						else {
							pTemp.setRemainingTime(pTemp.getRemainingTime()-qTime);
							huidigeTijd = huidigeTijd +qTime;
							
							//process kan niet klaar zijn
							//voeg het achteraan toe in de wachtlijst
							wachtLijst.addLast(pTemp);
						}
					//System.out.println("geraakt nietuit een lege wachtlijst");
					//einde van als de wachtlijst niet leeg is	
					}
				//System.out.println("geraakt aan 543");
				//System.out.println("size" + klaarLijst.size());
				//System.out.println("aantal elementen in wachtLijst" + wachtLijst.size());
				//einde van de while niet alle processen in klaar zitten
				}
			
			
			
			
			
		//System.out.println("geraakt aan 549");	
		//3) adhv de eindtijden kunnen we de rest telkens berekenen
		for(Process p : klaarLijst) {
			p.setWachttijd( p.getEindtijd() - p.getArrivalTime() - p.getServiceTime() );
			
			p.setTAT( p.getEindtijd() - p.getArrivalTime() );
			
			p.setnTAT( p.getTAT()/p.getServiceTime()  );
		}
		
	//System.out.println("geraakt hier");
	return klaarLijst;
	//einde methode bewerkprocessenRRQ8	
	}
	public ArrayList<Process> bewerkProcessenHRRN(ArrayList<Process> processen){
		
		//0.1) we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
		for (Process x: procesLijst) {
			x.setEindtijd(0);
			x.setWachttijd(0);
			x.setRemainingTime(x.getServiceTime());
			x.setnTAT(0);
			x.setTAT(0);
		}
		
		//0.2) procesLijst sorteren op arrivalTime
		Collections.sort(procesLijst, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});
		
		//1) definiëren van de nodige variabelen
			
			//1.1) lijsten
			ArrayList<Process> klaarLijst = new ArrayList<Process>();
			LinkedList<Process> wachtLijst = new LinkedList<Process>();
			
		
			//1.2) tijdelijk process
			Process pTemp;
			
			//1.3) totaal aantal processen
			int totaalAantalProcesen = procesLijst.size();
			
			//1.4) huidige tijd, die we setten op de tijd waarin het eerst proces zal binnenkomen
			int huidigeTijd = procesLijst.get(0).getArrivalTime();
			
			//1.5) de grootste nTAT variabelen
			int grootsteNTATid;
			double nTAT;

			
			
		//2) effectief algoritme
			//zolang de klaarlijst niet volledig gevuld is
		while(klaarLijst.size()!= totaalAantalProcesen) {
			
			
			//2.1) kijken als er voor het huidige tijdstip nog processen moeten binnekomen	
			while ( !procesLijst.isEmpty() &&huidigeTijd >= procesLijst.get(0).getArrivalTime() ) {
				
				wachtLijst.addLast(procesLijst.get(0));
				procesLijst.remove(0);
				
				
			}
			
			//2.2) logica voor de wachtLijst
				//2.2.1) als de wachtlijst leeg is
			if(wachtLijst.isEmpty()) {
				huidigeTijd = procesLijst.get(0).getArrivalTime();
			}
			
				//2.2.2) als er processen in de wachtlijst zitten
			else {
				
					//2.2.2.1) bereken voor alle processen in de wachtLijst de Response ratio
					for(Process p : wachtLijst) {
						p.setnTAT((huidigeTijd-p.getArrivalTime()+p.getServiceTime())/p.getServiceTime());	//nTAT wordt nog overschreven later op het einde voor alle processen in de klaarlijst
						
					}
					
					//2.2.2.1)	pak het process met de grootste nTAT, verwijder het uit de wachtlijst
					nTAT = 0;
					grootsteNTATid = -1;
					for(int i =0 ; i<wachtLijst.size() ; i++) {
						if(nTAT< wachtLijst.get(i).getnTAT()) {
							grootsteNTATid = i;
							nTAT = wachtLijst.get(i).getnTAT();
						}
					}
					
					pTemp = wachtLijst.remove(grootsteNTATid);
					
					//2.2.2.2) voer dit process volledig uit, voeg het toe aan de klaarlijst
					pTemp.setStarttijd(huidigeTijd);
					huidigeTijd = huidigeTijd + pTemp.getServiceTime();
					pTemp.setEindtijd(huidigeTijd);
					
					klaarLijst.add(pTemp);
					
					
			//einde van de 'als er processen in de wachtlijst zitten'	
			}
			
			
			
			
			
		//einde van de while lus , einde van het algoritme	
		}
		
		
		//3) adhv de eindtijden kunnen we de rest telkens berekenen
		for(Process p : klaarLijst) {
			p.setWachttijd( p.getEindtijd() - p.getArrivalTime() - p.getServiceTime() );
			
			p.setTAT( p.getEindtijd() - p.getArrivalTime() );
			
			p.setnTAT( p.getTAT()/p.getServiceTime()  );
		}
		
		
	//einde van de HRRN  methode
	return klaarLijst;
	}
	public ArrayList<Process> bewerkProcessenSRT(ArrayList<Process> processen){
		
		//0.1) we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
		for (Process x: procesLijst) {
			x.setEindtijd(0);
			x.setWachttijd(0);
			x.setRemainingTime(x.getServiceTime());
			x.setnTAT(0);
			x.setTAT(0);
		}
		
		//0.2) procesLijst sorteren op arrivalTime
		Collections.sort(procesLijst, new Comparator<Process>() {

			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getArrivalTime(), s2.getArrivalTime());
			}
		});
		
		//1) definieren van de nodige variabelen
			
			//1.1) lijsten
			ArrayList<Process> klaarLijst = new ArrayList<Process>();
			LinkedList<Process> wachtLijst = new LinkedList<Process>();
			
		
			//1.2) tijdelijk process
			Process pTemp;
			
			//1.3) totaal aantal processen
			int totaalAantalProcesen = procesLijst.size();
			
			//1.4) huidige tijd, die we setten op de tijd waarin het eerst proces zal binnenkomen
			int huidigeTijd = procesLijst.get(0).getArrivalTime();
			
			//1.5)  variabelen om de kortste remaining time te zoekn in de wachtLijst
			int srtId;
			int srt;
		
		//2) effectief algoritme
			//zolang de klaarlijst niet volledig gevuld is
			while(klaarLijst.size()!= totaalAantalProcesen) {

				//2.1) kijken als er voor het huidige tijdstip nog processen moeten binnekomen	
				while ( !procesLijst.isEmpty() &&huidigeTijd >= procesLijst.get(0).getArrivalTime() ) {
					
					wachtLijst.addLast(procesLijst.get(0));
					procesLijst.remove(0);
		
				
				}
				
				//2.2) logica voor de wachtLijst
					//2.2.1) als de wachtLijst leeg is
				if(wachtLijst.isEmpty()) {				
					huidigeTijd = procesLijst.get(0).getArrivalTime();
				}
					//2.2.2) als er items in de wachtlijst zitten
				else {
					
						//2.2.2.1) pak het  process met de kortste remaining time, execute het
							srtId= 0;
							srt = wachtLijst.get(0).getRemainingTime();
							for(int i =0 ; i<wachtLijst.size(); i++) {
								if(srt > wachtLijst.get(i).getRemainingTime()) {
									srt = wachtLijst.get(i).getRemainingTime();
									srtId =  i ;
								}
								
							}
							
							//eigenlijk niet nodig om het te removen. er is niet echt meer een lijst nodig				
							pTemp = wachtLijst.remove(srtId);

						//2.2.2.2)  check als het de eerste keer is datje het uitvoer
							if(pTemp.getRemainingTime() == pTemp.getServiceTime()) {pTemp.setStarttijd(huidigeTijd);}
				
							
						//2.2.2.3) voer het proces voor 1 jiffy uit	
							huidigeTijd++;
							pTemp.setRemainingTime(pTemp.getRemainingTime()-1);
							
							
						//2.2.2.4) terug in de lijst als  het niet klaar is
						//		   naar de klaarLijst als het wel klaar is
							if(pTemp.getRemainingTime()==0) {
								pTemp.setEindtijd(huidigeTijd);
								klaarLijst.add(pTemp);
							}
							else {
								wachtLijst.addLast(pTemp);
							}
					
				}
				
				
				
				
				
			//einde van het algoritme	
			}


			//3) adhv de eindtijden kunnen we de rest allemaal berekenen
			for(Process p : klaarLijst) {
				p.setWachttijd( p.getEindtijd() - p.getArrivalTime() - p.getServiceTime() );
				
				p.setTAT( p.getEindtijd() - p.getArrivalTime() );
				
				p.setnTAT( p.getTAT()/p.getServiceTime()  );
			}
		
	//einde van de SRT methode	
	return klaarLijst;
	}
}

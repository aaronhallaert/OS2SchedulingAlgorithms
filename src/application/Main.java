package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	Stage window;
	Scene keuzeMenu, grafiek;

	// defining the axes
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

	VBox layout1 = new VBox(20);

	// keuze nTAT of wachttijd
	ObservableList<String> soortOpties = FXCollections.observableArrayList("nTAT", "wachttijd");
	ComboBox soort = new ComboBox(soortOpties);

	// keuze aantal processen
	ObservableList<String> aantalOpties = FXCollections.observableArrayList("10.000 processen", "50.000 processen");
	ComboBox procesKeuze = new ComboBox(aantalOpties);

	Label error = new Label("");

	VBox layout2 = new VBox(20);

	Button terug;

	@Override
	public void start(Stage primaryStage) {
		lineChart.setCreateSymbols(false);
		lineChart.setPrefSize(300, 1100);
		window = primaryStage;
		window.setTitle("Scheduling Algorithms");

		////////////////////// keuze menu /////////////////////////////////

		Button generate = new Button("generate");
		generate.addEventHandler(ActionEvent.ACTION, (e) -> {
			lineChart.getData().clear();
			if (soort.getSelectionModel().isEmpty() || procesKeuze.getSelectionModel().isEmpty()) {
				error.setText("Gelieve een waarde aan te geven");
			} else {
				generateGrafiek();
				window.setScene(grafiek);
			}

		});

		layout1.setAlignment(Pos.CENTER);
		layout1.getChildren().addAll(procesKeuze, soort, generate, error);

		keuzeMenu = new Scene(layout1, 200, 200);

		///////////////////// grafiek /////////////////////////////////////

		terug = new Button("Terug");
		terug.setOnAction(e -> window.setScene(keuzeMenu));

		layout2.getChildren().add(lineChart);
		layout2.getChildren().add(terug);
		layout2.setAlignment(Pos.CENTER);
		grafiek = new Scene(layout2, 1000, 600);

		window.setScene(keuzeMenu);
		window.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void generateGrafiek() {

		xAxis.setLabel("Percentile of time required");

		if (soort.getValue().equals("nTAT")) {
			// of wachttijd
			yAxis.setLabel("nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			yAxis.setLabel("wachttijd");
		}

		String pad = null;
		if (procesKeuze.getValue().equals("10.000 processen")) {
			//pad = "D:/School/Industriele Ingenieurswetenschappen/iiw Ba3/Semester2/Besturingssystemen 2/processen10000.xml";
			pad="C:/Users/tibo/Documents/WorkspaceOS/processen10000.xml";
		} else if (procesKeuze.getValue().equals("50.000 processen")) {
			//pad = "D:/School/Industriele Ingenieurswetenschappen/iiw Ba3/Semester2/Besturingssystemen 2/processen50000.xml";
			pad="C:/Users/tibo/Documents/WorkspaceOS/processen50000.xml";
		}

		// hier worden alle processen ingelezen
		ArrayList<Process> processen = new ArrayList<Process>();
		findProcessen(pad, processen);

		// data FCFS inladen
		if (soort.getValue().equals("nTAT")) {
			addFCFS(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addFCFS(processen, "wachttijd");
		}

		// data SJF inladen
		if (soort.getValue().equals("nTAT")) {
			addSJF(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addSJF(processen, "wachttijd");
		}
		
		// data MLFB inladen
		if (soort.getValue().equals("nTAT")) {
			addMLFB(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addMLFB(processen, "wachttijd");
		}
		
		
		// data RR2 inladen
		if (soort.getValue().equals("nTAT")) {
			addRRQ2(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addRRQ2(processen, "wachttijd");
		}
		
		// data RR8 inladen
		if (soort.getValue().equals("nTAT")) {
			addRRQ8(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addRRQ8(processen, "wachttijd");
		}
		
		// data HRRN inladen
		if (soort.getValue().equals("nTAT")) {
			addHRRN(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addHRRN(processen, "wachttijd");
		}
		
		// data SRT inladen
		if (soort.getValue().equals("nTAT")) {
			addSRT(processen, "nTAT");
		} else if (soort.getValue().equals("wachttijd")) {
			addSRT(processen, "wachttijd");
		}
		 

		//// ANALOOG AAN HIERBOVEN ALGORITMES TOEVOEGEN //
		/*
		 * if(soort.getValue().equals("nTAT")) { addSJF(processen, "nTAT"); } else if
		 * (soort.getValue().equals("wachttijd")) { addSJF(processen, "wachttijd"); }
		 */

	}

	// niet preemptive
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
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
		// processen die uiteindelijk gereturned zullen worden
		ArrayList<Process> bewerkteProcessen = new ArrayList<Process>();
		
		// bijhouden van aangekomen processen
		ArrayList<Process> aangekomen = new ArrayList<Process>();

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

		
		// klok start bij het begin van het eerste proces
		int huidigeTijd = aangekomen.get(0).getArrivalTime();

		
		// zolang er nog processen zijn en er processen zijn aangekomen, voer uit...
		while (procesLijst.size() != 0 || aangekomen.size() != 0) {
			
			
			// nieuwe processen zoeken die zijn aangekomen en toevoegen aan de aangekomen lijst
			int tijd = huidigeTijd;
			ArrayList<Process> nieuweProcessen =new ArrayList<Process>();
			for(Process p: procesLijst) {
				if(p.getArrivalTime()<=tijd) {
					nieuweProcessen.add(p);
				}
			}
			
			// indien er geen processen meer aangekomen voor de huidige tijd, eerste proces uit processen opvragen en huidigeTijd shiften naar begin van dit proces
			if (nieuweProcessen.size() == 0 && aangekomen.size() == 0) {
				nieuweProcessen.add(procesLijst.get(0));
				huidigeTijd = procesLijst.get(0).getArrivalTime();
			}

			// toevoegen van nieuwe aangekomen processen 
			aangekomen.addAll(nieuweProcessen);
			// verwijderen uit processen
			procesLijst.removeAll(nieuweProcessen);

			
					
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

	public ArrayList<Process> bewerkProcessenMLFB(ArrayList<Process> processen) {
		
		// we maken een kopie van de binnenkomende processen en intializeren alles op 0 aangezien dit een doorgegeven lijst is en gebruikt werd door andere algoritmes
		
		ArrayList<Process> procesLijst= new ArrayList<Process>(processen);
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
		
		// we definieren de lengte van de timeslices in de wachtrijen
		int [] wachtrijLengte= {10,50,100};
		
		// init van bij te houden tijd en huidige proces
		int huidigeTijd;
		Process p;
		
		// te returnen list
		ArrayList<Process> bewerkteProcessen=new ArrayList<>();
		
		
		int q1=wachtrijLengte[0];
		int q2=wachtrijLengte[1];
		int q3=wachtrijLengte[2];
		int huidigeSlice;
		
		
		// queues initializeren
		Queue<Process> processqueue1=new LinkedList<>();
		Queue<Process> processqueue2=new LinkedList<>();
		Queue<Process> processqueue3=new LinkedList<>();
		Queue<Process> volgendeQueue;
		
		
		// initializeren van algoritme
		huidigeTijd = procesLijst.get(0).getArrivalTime();
		//toevoegen aan eerste queue
        processqueue1.add(procesLijst.remove(0));
        
        
        
        
        
        // zolang er nog processen aanwezig zijn, voer uit        
        while (!processqueue1.isEmpty() || !processqueue2.isEmpty() || !processqueue3.isEmpty() || !procesLijst.isEmpty()) {
        	
            //Volgende process selecteren
            if (!processqueue1.isEmpty()) {
                p = processqueue1.remove();
               
                huidigeSlice = q1;
                volgendeQueue = processqueue2;
            } else if (!processqueue2.isEmpty()) {
                p = processqueue2.remove();
                huidigeSlice = q2;
                volgendeQueue = processqueue3;
            } else if (!processqueue3.isEmpty()) {
                p = processqueue3.remove();
                huidigeSlice = q3;
                volgendeQueue = processqueue3;
            } else {
            	// als er geen processen meer in de queues zitten, haal een uit binnenkomende processen
                if (!procesLijst.isEmpty()) {
                	
                    p = procesLijst.remove(0);
                    huidigeTijd = p.getArrivalTime();
                   
                   
                    volgendeQueue = processqueue2;
                    huidigeSlice = q1;
                } else break;
            }
            
            p.setStarttijd(huidigeTijd); //Selected process gets startTime
            
            
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
                volgendeQueue.add(p);
            }

            int finalhuidigeTijd = huidigeTijd;
           
            LinkedList<Process> nextArrivedProcesses = new LinkedList<Process>();
            for(Process pr: procesLijst) {
            	
            	if (pr.getArrivalTime()<=finalhuidigeTijd) {
            		nextArrivedProcesses.add(pr);
            	}
            }
            
            procesLijst.removeAll(nextArrivedProcesses);
            processqueue1.addAll(nextArrivedProcesses);
            //currentProcess = null;

        }

        System.out.println("MLFB Completed");
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
	
	
	
	
	
	
	
	
	
	/// BEWERKPROCESSEN ALGORITME SCHRIJVEN (DIT IS HET BELANGRIJKSTE) ////////////

	public void addFCFS(ArrayList<Process> processen, String optie) {
		// series zijn alle punten
		XYChart.Series series = new XYChart.Series();
		series.setName("FCFS");

		// bewerkProcessenFCFS sorteert processen volgens arrivaltime, bepaalt wacht-,
		// eind-, start-... tijd
		processen = bewerkProcessenFCFS(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "FCFS");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "FCFS");
		}

	}

	public void addSJF(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("SJF");

		processen = bewerkProcessenSJF(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "SJF");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "SJF");
		}

	}

	public void addMLFB(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("MLFB");

		processen = bewerkProcessenMLFB(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "MLFB");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "MLFB");
		}

	}
	
	public void addRRQ2(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("RRQ2");

		processen = bewerkProcessenRRQ2(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "RRQ2");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "RRQ2");
		}

	}
	
	public void addRRQ8(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("RRQ8");

		processen = bewerkProcessenRRQ8(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "RRQ8");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "RRQ8");
		}

	}
	
	public void addHRRN(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("HRRN");

		processen = bewerkProcessenHRRN(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "HRRN");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "HRRN");
		}

	}
	
	public void addSRT(ArrayList<Process> processen, String optie) {
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("SRT");

		processen = bewerkProcessenSRT(processen);
		if (optie.equals("nTAT")) {
			plotnTAT(processen, series, "SRT");
		} else if (optie.equals("wachttijd")) {
			plotWachttijd(processen, series, "SRT");
		}

	}
	
	
	
	/////// HIER ADD ALGORITME FUNCTIE TOEVOEGEN OOK ANALOOG AAN HIERBOVEN /////

	public void plotnTAT(ArrayList<Process> processen, XYChart.Series series, String soort) {
		// De grafiek wordt verdeeld naargelang de servicetime van een proces, daarom
		// dienen we eerst de processen terug te sorteren
		// volgens servicetime
		Collections.sort(processen, new Comparator<Process>() {
			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getServiceTime(), s2.getServiceTime());
			}
		});

		// berekening voor gemiddelde waardes
		double somTAT = 0;
		double somnTAT = 0;
		double somWacht = 0;

		int aantalProcessen = processen.size();

		// percentielen

		// aantal processen per percentiel
		double aantalPerPercentiel = aantalProcessen / 10;

		double waardes[] = new double[100];
		double nTAT = 0;
		double hulp = 0;
		for (int x = 0; x < 100; x++) {
			for (double a = hulp; a < (hulp + aantalPerPercentiel / 10); a++) {
				somTAT += processen.get((int) a).getTAT();
				somnTAT += processen.get((int) a).getnTAT();
				somWacht += processen.get((int) a).getWachttijd();
				nTAT += processen.get((int) a).getnTAT();
			}
			waardes[x] = nTAT / (aantalPerPercentiel / 10);
			hulp = hulp + aantalPerPercentiel / 10;
			nTAT = 0;
		}
		for (int i = 1; i < waardes.length + 1; i++) {
			series.getData().add(new XYChart.Data(i, waardes[i - 1]));
		}
		// data toevoegen aan series
		lineChart.getData().add(series);

		double gemTAT = somTAT / (processen.size());
		double gemnTAT = somnTAT / (processen.size());
		double gemWacht = somWacht / (processen.size());
		System.out.println("---------------------------------");
		System.out.println(soort);
		System.out.println("---------------------------------");
		System.out.println("gemiddelde TAT " + gemTAT);
		System.out.println("gemiddelde nTAT " + gemnTAT);
		System.out.println("gemiddelde wachttijd " + gemWacht);
	}

	public void plotWachttijd(ArrayList<Process> processen, XYChart.Series series, String soort) {
		// De grafiek wordt verdeeld naargelang de servicetime van een proces, daarom
		// dienen we eerst de processen terug te sorteren
		// volgens servicetime
		Collections.sort(processen, new Comparator<Process>() {
			public int compare(Process s1, Process s2) {
				return Integer.compare(s1.getServiceTime(), s2.getServiceTime());
			}
		});

		// berekening voor gemiddelde waardes
		int somTAT = 0;
		int somnTAT = 0;
		int somWacht = 0;

		int aantalProcessen = processen.size();

		// percentielen

		// aantal processen per percentiel
		double aantalPerPercentiel = aantalProcessen / 10;

		double waardes[] = new double[100];
		double wacht = 0;
		double hulp = 0;
		for (int x = 0; x < 100; x++) {
			for (double a = hulp; a < (hulp + aantalPerPercentiel / 10); a++) {
				somTAT += processen.get((int) a).getTAT();
				somnTAT += processen.get((int) a).getnTAT();
				somWacht += processen.get((int) a).getWachttijd();
				wacht += processen.get((int) a).getWachttijd();
			}
			waardes[x] = wacht / (aantalPerPercentiel / 10);
			hulp = hulp + aantalPerPercentiel / 10;
			wacht = 0;
		}
		for (int i = 1; i < waardes.length + 1; i++) {
			series.getData().add(new XYChart.Data(i, waardes[i - 1]));
		}
		// data toevoegen aan series
		lineChart.getData().add(series);

		double gemTAT = somTAT / (processen.size());
		double gemnTAT = somnTAT / (processen.size());
		double gemWacht = somWacht / (processen.size());
		System.out.println("---------------------------------");
		System.out.println(soort);
		System.out.println("---------------------------------");
		System.out.println("gemiddelde TAT " + gemTAT);
		System.out.println("gemiddelde nTAT " + gemnTAT);
		System.out.println("gemiddelde wachttijd " + gemWacht);
	}

	public void findProcessen(String pad, ArrayList<Process> processen) {

		try {
			File xmlFile = new File(pad);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			// oplijsten van alle processen
			NodeList processList = doc.getElementsByTagName("process");

			for (int i = 0; i < processList.getLength(); i++) {
				// System.out.print(i+")");
				Node nNode = processList.item(i);
				// System.out.println(" Current Element: "+ nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					int PID = Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent());
					int arrivalTime = Integer
							.parseInt(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent());
					int serviceTime = Integer
							.parseInt(eElement.getElementsByTagName("servicetime").item(0).getTextContent());
					processen.add(new Process(PID, arrivalTime, serviceTime));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
package controller;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entities.Process;
import javafx.scene.chart.XYChart;
import presentatie.Grafiek;
import presentatie.KeuzeMenu;

public class DataProcessing {
public DataProcessing() {
	
}



//xml file analyseren en processen eruit halen
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
	
	
	public void generateGrafiek(KeuzeMenu keuzeMenu,Plot plot, Algoritmes algo, Grafiek grafiek) {
		
		grafiek.getxAxis().setLabel("Percentile of time required");

		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			// of wachttijd
			grafiek.getyAxis().setLabel("nTAT");
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			grafiek.getyAxis().setLabel("wachttijd");
		}

		String pad = null;
		if (keuzeMenu.getProcesKeuze().getValue().equals("10.000 processen")) {
			pad = "D:/School/Industriele Ingenieurswetenschappen/iiw Ba3/Semester2/Besturingssystemen 2/processen10000.xml";
		} else if (keuzeMenu.getProcesKeuze().getValue().equals("50.000 processen")) {
			pad = "D:/School/Industriele Ingenieurswetenschappen/iiw Ba3/Semester2/Besturingssystemen 2/processen50000.xml";
		}

		// hier worden alle processen ingelezen
		findProcessen(pad, algo.getProcessen());

		// data FCFS inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addFCFS( "nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addFCFS( "wachttijd", plot, algo, grafiek);
		}

		// data SJF inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addSJF( "nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addSJF( "wachttijd", plot, algo, grafiek);
		}
		
		// data RRQ2 inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addRRQ2("nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addRRQ2( "wachttijd", plot, algo, grafiek);
		}
		
		// data RRQ8 inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addRRQ8( "nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addRRQ8( "wachttijd", plot, algo, grafiek);
		}
		
		// data HRRN inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addHRRN( "nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addHRRN( "wachttijd", plot, algo, grafiek);
		}
		

		// data SRT inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addSRT( "nTAT", plot, algo, grafiek);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addSRT( "wachttijd", plot, algo, grafiek);
		}
		
		
		int lengtes[]=new int[3];
		
		lengtes[0]=128;
		lengtes[1]=256;
		lengtes[2]=512;
		System.out.println(lengtes[0]);
		// data MLFB inladen
		if (keuzeMenu.getSoort().getValue().equals("nTAT")) {
			addMLFB("nTAT", plot, algo, grafiek, lengtes);
		} else if (keuzeMenu.getSoort().getValue().equals("wachttijd")) {
			addMLFB( "wachttijd", plot, algo, grafiek, lengtes);
		}
		
	
		
	
		

	}
	
	// deze methodes zijn de commando's die de processen laten bewerken en plotten
		public void addFCFS( String optie, Plot plot, Algoritmes algo, Grafiek grafiek) {
			// series zijn alle punten
			XYChart.Series series = new XYChart.Series();
			series.setName("FCFS");

			// bewerkProcessenFCFS sorteert processen volgens arrivaltime, bepaalt wacht-,
			// eind-, start-... tijd
			ArrayList<Process> processen = algo.bewerkProcessenFCFS(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "FCFS", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "FCFS", grafiek);
			}

		}
		public void addSJF(String optie, Plot plot, Algoritmes algo, Grafiek grafiek ) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("SJF");

			ArrayList<Process> processen = algo.bewerkProcessenSJF(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "SJF", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "SJF", grafiek);
			}

		}
		public void addSRT(String optie, Plot plot, Algoritmes algo, Grafiek grafiek ) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("SRT");

			ArrayList<Process> processen = algo.bewerkProcessenSRT(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "SRT", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "SRT", grafiek);
			}

		}
		public void addMLFB( String optie, Plot plot, Algoritmes algo, Grafiek grafiek, int [] lengtes) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("MLFB");

			ArrayList<Process> processen = algo.bewerkProcessenMLFB(algo.getProcessen(), lengtes);
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "MLFB", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "MLFB", grafiek);
			}

		}
		public void addRRQ2( String optie, Plot plot, Algoritmes algo, Grafiek grafiek ) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("RRQ2");

			ArrayList<Process> processen = algo.bewerkProcessenRRQ2(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "RRQ2", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "RRQ2", grafiek);
			}

		}
		public void addRRQ8( String optie, Plot plot, Algoritmes algo, Grafiek grafiek ) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("RRQ8");

			ArrayList<Process> processen = algo.bewerkProcessenRRQ8(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "RRQ8", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "RRQ8", grafiek);
			}

		}
		public void addHRRN( String optie, Plot plot, Algoritmes algo, Grafiek grafiek ) {
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("HRRN");

			ArrayList<Process> processen = algo.bewerkProcessenHRRN(algo.getProcessen());
			if (optie.equals("nTAT")) {
				plot.plotnTAT(processen, series, "HRRN", grafiek);
			} else if (optie.equals("wachttijd")) {
				plot.plotWachttijd(processen, series, "HRRN", grafiek);
			}

		}
		
		
}

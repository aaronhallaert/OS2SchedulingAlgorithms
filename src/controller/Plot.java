package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import entities.Process;
import javafx.scene.chart.XYChart;
import presentatie.Grafiek;

public class Plot {

	
	public Plot() {
		
	}
	
	// plot methodes + gemiddelde berekenen
		public void plotnTAT(ArrayList<Process> processen, XYChart.Series series, String soort, Grafiek grafiek) {
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
			grafiek.getLineChart().getData().add(series);

			double gemTAT = somTAT / (processen.size());
			double gemnTAT = somnTAT / (processen.size());
			double gemWacht = somWacht / (processen.size());
			StringBuilder sb= new StringBuilder();
			sb.append(grafiek.getTextfield().getText());
			sb.append(System.getProperty("line.separator"));
			sb.append("---------------------------------").append(System.getProperty("line.separator"));
			sb.append(soort).append(System.getProperty("line.separator"));
			sb.append("---------------------------------").append(System.getProperty("line.separator"));
			sb.append("gemiddelde TAT " + gemTAT).append(System.getProperty("line.separator"));
			sb.append("gemiddelde nTAT " + gemnTAT).append(System.getProperty("line.separator"));
			sb.append("gemiddelde wachttijd " + gemWacht).append(System.getProperty("line.separator"));
			grafiek.getTextfield().setText(sb.toString());
			
			
			System.out.println("---------------------------------");
			System.out.println(soort);
			System.out.println("---------------------------------");
			System.out.println("gemiddelde TAT " + gemTAT);
			System.out.println("gemiddelde nTAT " + gemnTAT);
			System.out.println("gemiddelde wachttijd " + gemWacht);
		}
		public void plotWachttijd(ArrayList<Process> processen, XYChart.Series series, String soort, Grafiek grafiek) {
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
			grafiek.getLineChart().getData().add(series);

			double gemTAT = somTAT / (processen.size());
			double gemnTAT = somnTAT / (processen.size());
			double gemWacht = somWacht / (processen.size());
			
			
			StringBuilder sb= new StringBuilder();
			sb.append(grafiek.getTextfield().getText());
			sb.append(System.getProperty("line.separator"));
			sb.append("---------------------------------").append(System.getProperty("line.separator"));
			sb.append(soort).append(System.getProperty("line.separator"));
			sb.append("---------------------------------").append(System.getProperty("line.separator"));
			sb.append("gemiddelde TAT " + gemTAT).append(System.getProperty("line.separator"));
			sb.append("gemiddelde nTAT " + gemnTAT).append(System.getProperty("line.separator"));
			sb.append("gemiddelde wachttijd " + gemWacht).append(System.getProperty("line.separator"));
			grafiek.getTextfield().setText(sb.toString());
			
			
			System.out.println("---------------------------------");
			System.out.println(soort);
			System.out.println("---------------------------------");
			System.out.println("gemiddelde TAT " + gemTAT);
			System.out.println("gemiddelde nTAT " + gemnTAT);
			System.out.println("gemiddelde wachttijd " + gemWacht);
		}

}

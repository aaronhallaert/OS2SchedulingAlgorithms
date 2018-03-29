package presentatie;

import java.util.ArrayList;

import controller.Algoritmes;
import controller.DataProcessing;
import controller.Plot;
import entities.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeuzeMenu {
	Scene keuzeMenu;
	VBox layout1 = new VBox(20);

	// keuze nTAT of wachttijd
	ObservableList<String> soortOpties = FXCollections.observableArrayList("nTAT", "wachttijd");
	ComboBox soort = new ComboBox(soortOpties);

	// keuze aantal processen
	ObservableList<String> aantalOpties = FXCollections.observableArrayList("10.000 processen", "50.000 processen");
	ComboBox procesKeuze = new ComboBox(aantalOpties);

	Label error = new Label("");
	
	public KeuzeMenu(DataProcessing dp, Stage window, Grafiek grafiek, Plot plot, Algoritmes algo) {
		Button generate = new Button("generate");
		generate.addEventHandler(ActionEvent.ACTION, (e) -> {
			grafiek.getLineChart().getData().clear();
			if (soort.getSelectionModel().isEmpty() || procesKeuze.getSelectionModel().isEmpty()) {
				error.setText("Gelieve een waarde aan te geven");
			} else {
				dp.generateGrafiek(this, plot, algo, grafiek);
				window.setScene(grafiek.getGrafiekScene());
			}

		});

		layout1.setAlignment(Pos.CENTER);
		layout1.getChildren().addAll(procesKeuze, soort, generate, error);

		keuzeMenu = new Scene(layout1, 200, 200);
	}
	
	public Scene getScene() {
		return keuzeMenu;
	}

	public Scene getKeuzeMenu() {
		return keuzeMenu;
	}

	public void setKeuzeMenu(Scene keuzeMenu) {
		this.keuzeMenu = keuzeMenu;
	}

	public VBox getLayout1() {
		return layout1;
	}

	public void setLayout1(VBox layout1) {
		this.layout1 = layout1;
	}

	public ObservableList<String> getSoortOpties() {
		return soortOpties;
	}

	public void setSoortOpties(ObservableList<String> soortOpties) {
		this.soortOpties = soortOpties;
	}

	public ComboBox getSoort() {
		return soort;
	}

	public void setSoort(ComboBox soort) {
		this.soort = soort;
	}

	public ObservableList<String> getAantalOpties() {
		return aantalOpties;
	}

	public void setAantalOpties(ObservableList<String> aantalOpties) {
		this.aantalOpties = aantalOpties;
	}

	public ComboBox getProcesKeuze() {
		return procesKeuze;
	}

	public void setProcesKeuze(ComboBox procesKeuze) {
		this.procesKeuze = procesKeuze;
	}

	public Label getError() {
		return error;
	}

	public void setError(Label error) {
		this.error = error;
	}
	
	
	
}

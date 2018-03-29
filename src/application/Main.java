package application;

import controller.Algoritmes;
import controller.DataProcessing;
import controller.Plot;
import javafx.application.Application;
import javafx.stage.Stage;
import presentatie.Grafiek;
import presentatie.KeuzeMenu;

public class Main extends Application {
	
	Algoritmes algo= new Algoritmes();
	
	
	// dit object plot de grafieken
	Plot plot= new Plot();
	
	// inlezen en doorgeven van processen van algoritmes naar plotten gebeurt door dit object
	DataProcessing dp= new DataProcessing();
	// dit object voert algoritmes uit
	
	
	// (java fx)
	Stage window;
	KeuzeMenu keuzeMenu;
	Grafiek grafiek;
	

	@Override
	public void start(Stage primaryStage) {
		
		
		
		window = primaryStage;
		window.setTitle("Scheduling Algorithms");
		
		// aanmaken grafiek
		grafiek=new Grafiek(window);
		
		// aanmaken keuzemenu
		keuzeMenu=new KeuzeMenu(dp, window, grafiek, plot, algo);
		
		window.setScene(keuzeMenu.getScene());
		window.show();

	}

	public static void main(String[] args) {
		launch(args);
	}



	
	
	

	
	
	
}
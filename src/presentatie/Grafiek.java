package presentatie;



import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Grafiek {

	// defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		
		Button terug;
		BorderPane bp= new BorderPane();
		Label textField;
		VBox layout2 = new VBox(20);
		Scene grafiek;
		Window window;
		
	public Grafiek(Stage window) {
		
		lineChart.getStylesheets().add( this.getClass().getResource("application.css").toExternalForm());
		lineChart.getStyleClass().add("chart-series-line");
		lineChart.getStyleClass().add("chart-plot-background");
		// punten worden niet expliciet weergegeven
		lineChart.setCreateSymbols(false);
		lineChart.setPrefSize(500, 1600);
		textField= new Label();
		layout2.getChildren().add(lineChart);
		layout2.getChildren().add(textField);
		this.window=window;
		layout2.setAlignment(Pos.CENTER);
		bp.setCenter(layout2);
		bp.setRight(textField);
		
		Button buttonScale200= new Button("200");
		buttonScale200.addEventHandler(ActionEvent.ACTION, (e) -> {
			yAxis.setAutoRanging(false);
			yAxis.setUpperBound(200);
		});
		Button buttonScale500= new Button("500");
		buttonScale500.addEventHandler(ActionEvent.ACTION, (e) -> {
			yAxis.setAutoRanging(false);
			yAxis.setUpperBound(500);
		});
		Button buttonScale1000= new Button("1000");
		buttonScale1000.addEventHandler(ActionEvent.ACTION, (e) -> {
			yAxis.setAutoRanging(false);
			yAxis.setUpperBound(1000);
		});
		Button buttonScale1500= new Button("1500");
		buttonScale1500.addEventHandler(ActionEvent.ACTION, (e) -> {
			yAxis.setAutoRanging(false);
			yAxis.setUpperBound(1500);
		});
		Button buttonScaleAuto= new Button("Auto");
		
		buttonScaleAuto.addEventHandler(ActionEvent.ACTION, (e) -> {
			yAxis.setAutoRanging(true);
		});
		
		HBox buttons=new HBox();
		buttons.getChildren().addAll(buttonScale200,buttonScale500,buttonScale1000,buttonScale1500,buttonScaleAuto);
		
		bp.setTop(buttons);
		
		
		
		grafiek = new Scene(bp, 1500, 1000);
		
	}
	

	
	public Scene getGrafiekScene() {
		return grafiek;
	}

	public LineChart<Number, Number> getLineChart() {
		return lineChart;
	}

	public Button getTerug() {
		return terug;
	}

	public void setTerug(Button terug) {
		this.terug = terug;
	}

	public VBox getLayout2() {
		return layout2;
	}

	public void setLayout2(VBox layout2) {
		this.layout2 = layout2;
	}

	public Scene getGrafiek() {
		return grafiek;
	}

	public void setGrafiek(Scene grafiek) {
		this.grafiek = grafiek;
	}

	public NumberAxis getxAxis() {
		return xAxis;
	}

	public NumberAxis getyAxis() {
		return yAxis;
	}
	

	public Label getTextfield() {
		return textField;
	}

	
	public void setTextfield(Label t) {
		this.textField = t;
	}
	
	public Window getWindow() {
		return window;
	}
	
}

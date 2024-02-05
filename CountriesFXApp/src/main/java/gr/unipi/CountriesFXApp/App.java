package gr.unipi.CountriesFXApp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application implements EventHandler<MouseEvent> {
	
	//Stage
	Stage primaryStage;
	
	//Scenes
	Scene mainScene, CountriesScene;
	
	//FlowPane
	FlowPane rootFlowPane;
	
	//Buttons
	Button searchAll, searchByCountry, searchByLang, searchByCurrency;
	

	
	

    @Override
    public void start(Stage primaryStage) {
    	
    	this.primaryStage = primaryStage;
    	
    	rootFlowPane = new FlowPane();
    	
    	searchAll = new Button("Countries Worldwide");
    	searchByCountry = new Button("Choose country");
    	searchByLang = new Button("Choose language");
    	searchByCurrency = new Button("Choose currency");
    	
    	//setup flowpane
    	
    	rootFlowPane.getChildren().add(searchAll);
    	rootFlowPane.getChildren().add(searchByCountry);
    	rootFlowPane.getChildren().add(searchByLang);
    	rootFlowPane.getChildren().add(searchByCurrency);
    	
    	//attach handle event to searchAll
    	searchAll.setOnMouseClicked(this);    	

    	
    	//setup flowpane
    	
    	rootFlowPane.setHgap(10);
    	rootFlowPane.setAlignment(Pos.CENTER);
    	searchAll.setMinSize(150, 40);
    	searchByCountry.setMinSize(150, 40);
    	searchByLang.setMinSize(150, 40);
    	searchByCurrency.setMinSize(150, 40);
    	
    	mainScene = new Scene(rootFlowPane, 850, 300);
    	
    	
    	primaryStage.setTitle("Country Information Searching");    	
    	primaryStage.setScene(mainScene);
    	primaryStage.show();
    }
    
    @Override
    public void handle(MouseEvent event) {
    	if(event.getSource() == searchAll) {
    		primaryStage.setTitle("All Countries Information");
    		primaryStage.setScene(CountriesScene);
    	}
    	
    }

    public static void main(String[] args) {
        launch();
    }

}
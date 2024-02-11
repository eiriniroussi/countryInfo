package gr.unipi.CountriesFXApp;
import com.jfoenix.controls.JFXButton;
import java.net.URISyntaxException;

import exception.countriesAPIException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.countryInfo;
import service.deserializeData;

public class AllCountriesSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
	
	//Initialize buttons, text fields, table of data, popup for search suggestions and gridpane
    

    GridPane rootGridPane, inputFieldsPane;

    Button searchBtn, goBackBtn;

    TableView<countryInfo> showAllcountriesView;
    
    //New dererializeData for using the method of class deserializeData
    deserializeData countryService = new deserializeData("https://restcountries.com");
  
	
	public AllCountriesSceneCreator(double width, double height) {
		super(width,height);
		
        //Setup
        rootGridPane = new GridPane();

        searchBtn = new Button("Display All");
        goBackBtn = new Button("Go Back");

        showAllcountriesView = new TableView<countryInfo>();

        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(searchBtn, goBackBtn);
        buttonHBox.setAlignment(Pos.CENTER);

        rootGridPane.add(buttonHBox, 1, 1);

        rootGridPane.add(showAllcountriesView, 0, 0, 1, 2);

        createTableColumns();

        goBackBtn.setOnMouseClicked(this);
        searchBtn.setOnMouseClicked(this);
        goBackBtn.setStyle("-fx-background-color: #ff1744; -fx-text-fill: white;");
        searchBtn.setStyle("-fx-background-color: #198754; -fx-text-fill: white;");
	}
	
    //Create tableColumns
	private void createTableColumns() {
	    String[] columnTitles = {"Country", "Capital", "Population", "Continent", "Currencies"};
	    String[] propertyNames = {"name", "capital", "population", "continents", "formattedCurrencies"};

	    for (int i = 0; i < columnTitles.length; i++) {
	        final int finalI = i; // Create a final copy of the variable
	        TableColumn<countryInfo, String> column = new TableColumn<>(columnTitles[i]);

	        if (propertyNames[finalI].equals("capital") || propertyNames[finalI].equals("continents") || propertyNames[finalI].equals("formattedCurrencies")) {
	            column.setCellValueFactory(cellData -> {
	                countryInfo country = cellData.getValue();
	                switch (propertyNames[finalI]) {
	                    case "capital":
	                        return new SimpleStringProperty(country.getCapital() != null ? String.join(", ", country.getCapital()) : "No Capital");
	                    case "continents":
	                        return new SimpleStringProperty(country.getContinents() != null ? String.join(", ", country.getContinents()) : "No Continent");
	                    case "formattedCurrencies":
	                        return new SimpleStringProperty(country.getFormattedCurrencies() != null ? String.join(", ", country.getFormattedCurrencies()) : "No Currency");
	                    default:
	                        return new SimpleStringProperty("");
	                }
	            });
	        } else {
	            column.setCellValueFactory(new PropertyValueFactory<>(propertyNames[finalI]));
	        }

	        showAllcountriesView.getColumns().add(column);
	    }
	}
	
	@Override
	Scene createScene() {
        rootGridPane.setStyle("-fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;");
        showAllcountriesView.setStyle("-fx-pref-height: 200;");
        showAllcountriesView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rootGridPane.setAlignment(Pos.CENTER);

        return new Scene(rootGridPane, width, height);
		
	}

    //Handling of events
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == searchBtn) {
                try {
                    countryInfo[] countries = countryService.searchAllCountries("all");
                    if (countries != null && countries.length > 0) {
                    	showAllcountriesView.getItems().clear();
                    	showAllcountriesView.getItems().addAll(countries);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No countries found!");
                    }
                } catch (countriesAPIException | URISyntaxException e) {
                
                     e.printStackTrace();
                     showAlert(Alert.AlertType.ERROR, "API Error", "An error occurred while fetching data: " + e.getMessage());
                }
            
        }else if (event.getSource() == goBackBtn) {
            App.primaryStage.setScene(App.mainScene);
            App.primaryStage.setTitle("Country Information Searching");
        }
    }
    

    
    //Alert method
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}





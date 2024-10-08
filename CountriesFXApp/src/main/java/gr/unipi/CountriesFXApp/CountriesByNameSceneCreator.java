package gr.unipi.CountriesFXApp;

import java.net.URISyntaxException;
import java.util.ArrayList;

import exception.countriesAPIException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.countryInfo;
import service.deserializeData;

public class CountriesByNameSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
	
	//Initialize buttons, text fields, table of data, popup for search suggestions and gridpane

    private final ArrayList<String> lastFiveValidInputs = new ArrayList<>();
    
    //Suggestion popup for last searches
    private ContextMenu suggestionsMenu;

    GridPane rootGridPane, inputFieldsPane;

    Button searchBtn, goBackBtn;

    Label nameLbl;

    TextField nameField;

    TableView<countryInfo> searchByNameView;
    
    //New dererializeData for using the method of class deserializeData
    deserializeData countryService = new deserializeData("https://restcountries.com");

	public CountriesByNameSceneCreator(double width, double height) {
		super(width, height);
		
		   //Setup
        rootGridPane = new GridPane();

        nameLbl = new Label("Search by Country Name:");
        nameField = new TextField();
        setupTextFieldAndContextMenu();

        searchBtn = new Button("Search");
        goBackBtn = new Button("Go Back");

        inputFieldsPane = new GridPane();
        searchByNameView = new TableView<countryInfo>();

        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(searchBtn, goBackBtn);
        buttonHBox.setAlignment(Pos.CENTER);

        rootGridPane.add(buttonHBox, 1, 1);

        inputFieldsPane.add(nameLbl, 1, 0);
        inputFieldsPane.add(nameField, 1, 1);
        inputFieldsPane.setAlignment(Pos.TOP_LEFT);
        inputFieldsPane.setHgap(15);
        inputFieldsPane.setVgap(15);

        rootGridPane.add(inputFieldsPane, 1, 0);
        rootGridPane.add(searchByNameView, 0, 0, 1, 2);

        createTableColumns();


        nameField.setMinSize(200,35);
        goBackBtn.setMinSize(85,35);
        searchBtn.setMinSize(85,35);
        goBackBtn.setOnMouseClicked(this);
        searchBtn.setOnMouseClicked(this);
        goBackBtn.getStyleClass().addAll("button", "go-back-button");
        searchBtn.getStyleClass().addAll("button");
        nameField.getStyleClass().addAll("text-field");
        nameLbl.getStyleClass().addAll("label");
       
	    
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

	        searchByNameView.getColumns().add(column);
	    }
	}
    
    
    //Setup for the search suggestion popup
    private void setupTextFieldAndContextMenu() {
        suggestionsMenu = new ContextMenu();
        nameField.setContextMenu(suggestionsMenu);
        nameField.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && !lastFiveValidInputs.isEmpty()) {
                suggestionsMenu.getItems().clear();
                for (String input : lastFiveValidInputs) {
                    MenuItem item = new MenuItem(input);
                    item.setOnAction(ev -> nameField.setText(input));
                    suggestionsMenu.getItems().add(item);
                }
                suggestionsMenu.show(nameField, Side.BOTTOM, 0, 0);
            }
        });
    }
	
	
	
    
    //Customize the scene
    @Override
    Scene createScene() {
        rootGridPane.setStyle("-fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;-fx-background-color: #cfe2ff;");
        searchByNameView.setStyle("-fx-pref-height: 200;-fx-pref-width: 500;");
        searchByNameView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rootGridPane.setAlignment(Pos.CENTER);
        nameField.setPromptText("Enter country name");
        rootGridPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return new Scene(rootGridPane, width, height);

    }
    
    
    //Keep or remove the inputs until they are 5
    private void addToLastFiveValidInputs(String input) {
        if (lastFiveValidInputs.contains(input)) {
            lastFiveValidInputs.remove(input);
        }
        lastFiveValidInputs.add(0, input);
        if (lastFiveValidInputs.size() > 5) {
            lastFiveValidInputs.remove(5);
        }
    }
    
    //Check to see if the input is valid text
    private boolean isValidLanguageInput(String input) {
        return input != null && !input.trim().isEmpty() && input.matches("[a-zA-Z]+");
    }
    
    //Alert method
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    //Handling of events
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == searchBtn) {
            String name = nameField.getText();

            if (!isValidLanguageInput(name)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid text for the country name.");
            } else {
                try {
                    countryInfo[] countries = countryService.getCountryByLang("name", name);
                    if (countries != null && countries.length > 0) {
                        searchByNameView.getItems().clear();
                        searchByNameView.getItems().addAll(countries);
                        addToLastFiveValidInputs(name);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No countries found for the specified name.");
                    }
                } catch (countriesAPIException | URISyntaxException e) {
                    if (e.getMessage().contains("\"status\":404")) {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "Please try again! There is no country with the name: " + name);
                    } else {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "API Error", "An error occurred while fetching data: " + e.getMessage());
                    }
                }
            }
        } else if (event.getSource() == goBackBtn) {
            App.primaryStage.setScene(App.mainScene);
            App.primaryStage.setTitle("Country Information Searching");
        }
    }
}

    
    
	    
	
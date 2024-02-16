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


public class CountryByLanguageSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
	
	
	//Initialize buttons, text fields, table of data, popup for search suggestions and gridpane

    private final ArrayList<String> lastFiveValidInputs = new ArrayList<>();
    
    //Suggestion popup for last searches
    private ContextMenu suggestionsMenu;

    GridPane rootGridPane, inputFieldsPane;

    Button searchBtn, goBackBtn;

    Label languageLbl;

    TextField languageField;

    TableView<countryInfo> searchByLanguageView;
    
    //New dererializeData for using the method of class deserializeData
    deserializeData countryService = new deserializeData("https://restcountries.com");
    
    
    //Constructor

    public CountryByLanguageSceneCreator(double width, double height) {
        super(width, height);
        
        
        //Setup
        rootGridPane = new GridPane();

        languageLbl = new Label("Search by Country's Language:");
        languageField = new TextField();
        setupTextFieldAndContextMenu();

        searchBtn = new Button("Search");
        goBackBtn = new Button("Go Back");

        inputFieldsPane = new GridPane();
        searchByLanguageView = new TableView<countryInfo>();

        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(searchBtn, goBackBtn);
        buttonHBox.setAlignment(Pos.CENTER);

        rootGridPane.add(buttonHBox, 1, 1);

        inputFieldsPane.add(languageLbl, 1, 0);
        inputFieldsPane.add(languageField, 1, 1);
        inputFieldsPane.setAlignment(Pos.TOP_LEFT);
        inputFieldsPane.setHgap(15);
        inputFieldsPane.setVgap(15);

        rootGridPane.add(inputFieldsPane, 1, 0);
        rootGridPane.add(searchByLanguageView, 0, 0, 1, 2);

        createTableColumns();

        goBackBtn.setOnMouseClicked(this);
        searchBtn.setOnMouseClicked(this);
        goBackBtn.getStyleClass().addAll("button", "go-back-button");
        searchBtn.getStyleClass().addAll("button");
        languageField.getStyleClass().addAll("text-field");
        languageLbl.getStyleClass().addAll("label");
        languageField.setMinSize(200,35);
        goBackBtn.setMinSize(85,35);
        searchBtn.setMinSize(85,35);

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

	        searchByLanguageView.getColumns().add(column);
	    }
	}
    
    
    //Customize the scene
    Scene createScene() {
        rootGridPane.setStyle("-fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;-fx-background-color: #cfe2ff;");
        searchByLanguageView.setStyle("-fx-pref-height: 200;-fx-pref-width: 500;");
        searchByLanguageView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rootGridPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        rootGridPane.setAlignment(Pos.CENTER);
        languageField.setPromptText("Enter language");

        return new Scene(rootGridPane, width, height);
    }
    
    
    //Setup for the search suggestion popup
    private void setupTextFieldAndContextMenu() {
        suggestionsMenu = new ContextMenu();
        languageField.setContextMenu(suggestionsMenu);
        languageField.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && !lastFiveValidInputs.isEmpty()) {
                suggestionsMenu.getItems().clear();
                for (String input : lastFiveValidInputs) {
                    MenuItem item = new MenuItem(input);
                    item.setOnAction(ev -> languageField.setText(input));// takes an EventHandler<ActionEvent> as its argument ev
                    suggestionsMenu.getItems().add(item);
                }
                suggestionsMenu.show(languageField, Side.BOTTOM, 0, 0);
            }
        });
    }
    
    
    //Handling of events
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == searchBtn) {
            String language = languageField.getText();

            if (!isValidLanguageInput(language)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid text for the language.");
            } else {
                try {
                    countryInfo[] countries = countryService.getCountryByLang("lang", language);
                    if (countries != null && countries.length > 0) {
                        searchByLanguageView.getItems().clear();
                        searchByLanguageView.getItems().addAll(countries);
                        addToLastFiveValidInputs(language);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No countries found for the specified language.");
                    }
                } catch (countriesAPIException | URISyntaxException e) {
                    if (e.getMessage().contains("\"status\":404")) {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No data available for the language: " + language);
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
        Alert alert = new Alert(alertType);//create a new alert
        alert.setTitle(title);//set the window title
        alert.setHeaderText(null);//header text of the alert
        alert.setContentText(content);//Alert main text
        alert.showAndWait();//Display the alert and wait for someone to close it
    }
}




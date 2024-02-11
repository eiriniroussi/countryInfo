package gr.unipi.CountriesFXApp;

import java.net.URISyntaxException;
import java.util.ArrayList;

import exception.countriesAPIException;
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

public class CountriesByCurrencyScene extends SceneCreator implements EventHandler<MouseEvent> {
	
	//Initialize buttons, text fields, table of data, popup for search suggestions and gridpane

    private final ArrayList<String> lastFiveValidInputs = new ArrayList<>();
    
    //Suggestion popup for last searches
    private ContextMenu suggestionsMenu;

    GridPane rootGridPane, inputFieldsPane;

    Button searchBtn, goBackBtn;

    Label currencyLbl;

    TextField currencyField;

    TableView<countryInfo> searchByCurrencyView;
    
    //New dererializeData for using the method of class deserializeData
    deserializeData countryService = new deserializeData("https://restcountries.com");

	public CountriesByCurrencyScene(double width, double height) {
		super(width, height);
		   //Setup
        rootGridPane = new GridPane();

        currencyLbl = new Label("Currency");
        currencyField = new TextField();
        setupTextFieldAndContextMenu();

        searchBtn = new Button("Search");
        goBackBtn = new Button("Go Back");

        inputFieldsPane = new GridPane();
        searchByCurrencyView = new TableView<countryInfo>();

        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(searchBtn, goBackBtn);
        buttonHBox.setAlignment(Pos.CENTER);

        rootGridPane.add(buttonHBox, 1, 1);

        inputFieldsPane.add(currencyLbl, 1, 0);
        inputFieldsPane.add(currencyField, 1, 1);
        inputFieldsPane.setAlignment(Pos.TOP_LEFT);
        inputFieldsPane.setHgap(15);
        inputFieldsPane.setVgap(15);

        rootGridPane.add(inputFieldsPane, 1, 0);
        rootGridPane.add(searchByCurrencyView, 0, 0, 1, 2);

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
            TableColumn<countryInfo, String> column = new TableColumn<>(columnTitles[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            searchByCurrencyView.getColumns().add(column);
        }
    }
    
    
    //Setup for the search suggestion popup
    private void setupTextFieldAndContextMenu() {
        suggestionsMenu = new ContextMenu();
        currencyField.setContextMenu(suggestionsMenu);
        currencyField.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && !lastFiveValidInputs.isEmpty()) {
                suggestionsMenu.getItems().clear();
                for (String input : lastFiveValidInputs) {
                    MenuItem item = new MenuItem(input);
                    item.setOnAction(ev -> currencyField.setText(input));
                    suggestionsMenu.getItems().add(item);
                }
                suggestionsMenu.show(currencyField, Side.BOTTOM, 0, 0);
            }
        });
    }
    
  //Customize the scene
    @Override
    Scene createScene() {
        rootGridPane.setStyle("-fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;");
        searchByCurrencyView.setStyle("-fx-pref-height: 200;");
        searchByCurrencyView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rootGridPane.setAlignment(Pos.CENTER);
        currencyField.setPromptText("Enter country name");

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
    private boolean isValidCurrencyInput(String input) {
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
            String currency = currencyField.getText();

            if (!isValidCurrencyInput(currency)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid currency code.");
            } else {
                try {
                    countryInfo[] countries = countryService.getCountryByCurrency("currency", currency);
                    if (countries != null && countries.length > 0) {
                        searchByCurrencyView.getItems().clear();
                        searchByCurrencyView.getItems().addAll(countries);
                        addToLastFiveValidInputs(currency);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No countries found for the specified name.");
                    }
                } catch (countriesAPIException | URISyntaxException e) {
                    if (e.getMessage().contains("\"status\":404")) {
                        showAlert(Alert.AlertType.INFORMATION, "No Results", "No data available for the country: " + currency);
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



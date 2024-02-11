package gr.unipi.CountriesFXApp;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class MainSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {

    private VBox root;
    private Button searchAll, searchByCountry, searchByLang, searchByCurrency;

    public MainSceneCreator(double width, double height) {
        super(width, height);

        // FontAwesome icon earth
        FontAwesomeIconView globeIconView = new FontAwesomeIconView(FontAwesomeIcon.GLOBE);
        globeIconView.setStyle("-fx-font-size: 28px; -fx-fill: #0d6efd;");

        // Title label with icon earth
        Label titleLabel = new Label("Country Information Portal");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-padding: 10px; -fx-alignment: center;");
        HBox titleBox = new HBox(5, globeIconView, titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        // FlowPane for buttons
        FlowPane buttonFlowPane = new FlowPane();
        buttonFlowPane.setAlignment(Pos.CENTER);
        buttonFlowPane.setHgap(10);
        buttonFlowPane.setVgap(10);

        // Buttons initialization
        searchAll = new Button("List All Countries");
        searchByCountry = new Button("Country Lookup");
        searchByLang = new Button("Language-based Search");
        searchByCurrency = new Button("Currency-based Search");

        // Style and event handlers for buttons
        styleAndAttachHandlers(searchAll);
        styleAndAttachHandlers(searchByCountry);
        styleAndAttachHandlers(searchByLang);
        styleAndAttachHandlers(searchByCurrency);

        // Add buttons to FlowPane
        buttonFlowPane.getChildren().addAll(searchAll, searchByCountry, searchByLang, searchByCurrency);

        // VBox as root container
        root = new VBox(10, titleBox, buttonFlowPane);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #cfe2ff;");
    }
    
    
    //Method for styling and attaching event to the button
    private void styleAndAttachHandlers(Button button) {
        button.setStyle("-fx-background-color: #0d6efd; -fx-text-fill: white; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-size: 16px; -fx-font-family: 'Arial'");
        button.setOnMouseClicked(this);
        button.setMinSize(150, 40);
    }

    //Create scene
    @Override
    Scene createScene() {
        return new Scene(root, width, height);
    }


    //Handle events based on which button being clicked
    @Override
    public void handle(MouseEvent event) {
        Object source = event.getSource();

        if (source == searchAll) {
            App.primaryStage.setTitle("All countries information");
            App.primaryStage.setScene(App.countriesAllScene);
        } else if (source == searchByCountry) {
            App.primaryStage.setTitle("Search countries by country name");
            App.primaryStage.setScene(App.countriesByNameScene);
        } else if (source == searchByLang) {
            App.primaryStage.setTitle("Search countries by language");
            App.primaryStage.setScene(App.countriesByLanguageScene);
        } else if (source == searchByCurrency) {
            App.primaryStage.setTitle("Search countries by currency");
            App.primaryStage.setScene(App.CountriesByCurrencyScene);
        }
    }

}

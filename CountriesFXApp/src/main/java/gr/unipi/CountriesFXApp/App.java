package gr.unipi.CountriesFXApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application{
	
	//Stage
	static Stage primaryStage;
	
	//Scenes
	static Scene mainScene, countriesAllScene, countriesByLanguageScene, countriesByNameScene, CountriesByCurrencyScene;
	
    @Override
    public void start(Stage primaryStage) {
    	
    	App.primaryStage = primaryStage;
    	
    	SceneCreator mainSceneCreator = new MainSceneCreator(800,300);
    	mainScene = mainSceneCreator.createScene();
    	
    	primaryStage.setTitle("Country Information Searching");    	
    	primaryStage.setScene(mainScene);
    	primaryStage.show();
    	
    	
    	SceneCreator countriesByLanguage = new CountryByLanguageSceneCreator(800,300);
    	countriesByLanguageScene = countriesByLanguage.createScene();
    	
    	SceneCreator countriesByName = new CountriesByNameSceneCreator(800,300);
    	countriesByNameScene = countriesByName.createScene();
    	
    	SceneCreator allCountries = new AllCountriesSceneCreator(800,300);
    	countriesAllScene = allCountries.createScene();
    	
    	SceneCreator countriesByCurrency = new CountriesByCurrencyScene(800,300);
    	CountriesByCurrencyScene = countriesByCurrency.createScene();
    	
    }

    public static void main(String[] args) {
        launch();
    }

}
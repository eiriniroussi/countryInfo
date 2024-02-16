import org.junit.BeforeClass;
import org.junit.Test;

import gr.unipi.CountriesFXApp.MainSceneCreator;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import static org.junit.Assert.*;
import javafx.scene.Scene;

public class MainSceneCreatorTest {

    @BeforeClass
    public static void initToolkit() throws InterruptedException {
        // Initializes JavaFX environment
        new JFXPanel();

        // Ensures JavaFX runtime is initialized
        Platform.runLater(() -> {});
    }

    @Test
    public void testCreateScene() {
        // Create your scene under the JavaFX thread
        Platform.runLater(() -> {
            MainSceneCreator mainSceneCreator = new MainSceneCreator(800, 300);
            Scene scene = mainSceneCreator.createScene();
            assertNotNull(scene);
        });
    }
    //With this test we validated that a scene was created using MainSceneCreator!
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.Design.UserInterface;

public class Main extends Application
{
	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		UserInterface uiWorkspace = new UserInterface();

        // Created with a standard 1200x800 window
        Scene scene = new Scene(uiWorkspace.createContent(), 1000, 500);

        primaryStage.setTitle("DevFilter AI - Dashboard");
        primaryStage.setScene(scene);
        
        // Allow the user to see the full design
        primaryStage.show();
		
	}

}

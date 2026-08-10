import data.Structures.Graph;
import data.Training.TrainingSample;
import image.Classify.Classifyer;
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
	
		TrainingSample trainingSample = new TrainingSample();

	    // Load and train using dataset/InvalidCv and dataset/ValidCv
	    trainingSample.train();

	    Graph trainingGraph = trainingSample.getDatabaseGraph();

	    // Create classifier using the trained graph
	    Classifyer classifier = new Classifyer(trainingGraph);

	    // Give the trained graph and classifier to the UI
	    UserInterface uiWorkspace =
	            new UserInterface(trainingGraph, classifier);

	    Scene scene = new Scene(
	            uiWorkspace.createContent(),
	            1000,
	            500
	    );

	    primaryStage.setTitle("DevFilter AI - Dashboard");
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
	}

}

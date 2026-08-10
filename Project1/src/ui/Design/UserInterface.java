package ui.Design;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.scene.input.TransferMode;
import java.io.File;
import java.util.Map;
import data.Structures.Graph;
import image.Classify.ClassificationTask;
import image.Classify.Classifyer;



import feature.Extraction.FeatureExtracter;

public class UserInterface {

	private StackPane contentArea;
	private HBox stepBar;
	private boolean isUploaded = false;

	
	private File lastUploadedFile;
	private FeatureExtracter extractor = new FeatureExtracter();
	
	private String finalStatus;
	private String recommendation;
	private String knnLabel;
	private int clusterCount;

	private Button btnUpload;
	private Button btnStructure;
	private Button btnScore;
	private Button btnRecommendations;
	
	private Graph trainingGraph;
	private Classifyer classifier;
	
	public UserInterface(Graph trainingGraph, Classifyer classifier) {
	    this.trainingGraph = trainingGraph;
	    this.classifier = classifier;
	}
	public void setClassificationResult(
	        String finalStatus,
	        String recommendation,
	        String knnLabel,
	        int clusterCount) {

	    this.finalStatus = finalStatus;
	    this.recommendation = recommendation;
	    this.knnLabel = knnLabel;
	    this.clusterCount = clusterCount;
	}
	

	public Parent createContent() {
		VBox root = new VBox();
		root.getStyleClass().add("root-container");
		root.setPrefSize(850, 650);

		String css = getClass().getResource("/ui/Design/style.css").toExternalForm();
		root.getStylesheets().add(css);

		
		HBox navbar = new HBox(10);
		navbar.getStyleClass().add("navbar");
		navbar.setAlignment(Pos.CENTER_LEFT);

		VBox logoText = new VBox(new Label("DevFilter AI"), new Label("ATS Resume Intelligence"));
		((Label) logoText.getChildren().get(0)).getStyleClass().add("logo-title");
		((Label) logoText.getChildren().get(1)).getStyleClass().add("logo-subtitle");

		Region navSpacer = new Region();
		HBox.setHgrow(navSpacer, Priority.ALWAYS);

		HBox statusBox = new HBox(8);
		statusBox.getStyleClass().add("status-box");
		statusBox.setAlignment(Pos.CENTER);
		Region pulseDot = new Region();
		pulseDot.getStyleClass().add("pulse-dot");
		Label statusLabel = new Label("ATS Engine Online");
		statusLabel.getStyleClass().add("status-label");
		statusBox.getChildren().addAll(pulseDot, statusLabel);
		navbar.getChildren().addAll(logoText, navSpacer, statusBox);

		
		stepBar = new HBox(12);
		stepBar.getStyleClass().add("steps-container");
		stepBar.setAlignment(Pos.CENTER);

		btnUpload = new Button("1. Upload CV");
		btnStructure = new Button("2. Structure");
		btnScore = new Button("3. ATS Score");
		btnRecommendations = new Button("4. Recommendations");

		btnUpload.getStyleClass().add("step-item");
		btnStructure.getStyleClass().add("step-item");
		btnScore.getStyleClass().add("step-item");
		btnRecommendations.getStyleClass().add("step-item");

		btnUpload.setOnAction(e -> {
			showInitialUploadView();
			handleFileSelection(root);
		});

		btnStructure.setOnAction(e -> {
			if (isUploaded)
				showStructureView();
		});
		btnScore.setOnAction(e -> {
			if (isUploaded)
				showScoreView( finalStatus,
		                recommendation,
		                knnLabel,
		                clusterCount);
		});
		btnRecommendations.setOnAction(e -> {
			if (isUploaded)
				showRecommendationsView();
		});

		stepBar.getChildren().addAll(btnUpload, btnStructure, btnScore, btnRecommendations);

		contentArea = new StackPane();
		VBox panel = new VBox();
		panel.getStyleClass().add("main-panel");
		panel.setMaxWidth(800);
		panel.getChildren().add(contentArea);

		VBox mainLayout = new VBox(panel);
		mainLayout.setAlignment(Pos.TOP_CENTER);
		mainLayout.setPadding(new Insets(5, 15, 25, 15));

		ScrollPane scrollPane = new ScrollPane(mainLayout);
		scrollPane.getStyleClass().add("main-scroll-pane");
		scrollPane.setFitToWidth(true);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		root.getChildren().addAll(navbar, stepBar, scrollPane);
		showInitialUploadView();

		return root;
	}

	private void handleFileSelection(VBox root) {
		 FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Select Resume PDF");
		    fileChooser.getExtensionFilters().add(
		        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
		    );

		    File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());

		    if (selectedFile != null) {
		        isUploaded = true;
		        lastUploadedFile = selectedFile;

		        showUploadSuccessView();

		        ClassificationTask task = new ClassificationTask(
		            selectedFile,
		            this,
		            trainingGraph,
		            classifier
		        );

		        Thread classificationThread = new Thread(task);
		        classificationThread.setDaemon(true);
		        classificationThread.start();
		    }
	}

	private void showInitialUploadView() {
		resetButtonStyles();
		btnUpload.getStyleClass().add("active");

		VBox view = new VBox(10);
		Label title = new Label("Upload Your Technical Resume");
		title.getStyleClass().add("view-title");
		Label desc = new Label("Analyze your resume against simulated ATS parsing systems.");
		desc.getStyleClass().add("view-desc");

		VBox uploadBox = new VBox(8);
		uploadBox.getStyleClass().add("upload-box");
		uploadBox.setAlignment(Pos.CENTER);
		Label icon = new Label("📄");
		icon.getStyleClass().add("upload-box-icon");
		Label mainPrompt = new Label("Click or Drag & Drop Resume");
		mainPrompt.getStyleClass().add("upload-box-prompt");
		Label subPrompt = new Label("Only PDF supported");
		uploadBox.getChildren().addAll(icon, mainPrompt, subPrompt);

		uploadBox.setOnMouseClicked(e -> handleFileSelection(view));

		uploadBox.setOnDragOver(event -> {
			if (event.getDragboard().hasFiles())
				event.acceptTransferModes(TransferMode.COPY);
			event.consume();
		});

		uploadBox.setOnDragDropped(event -> {
			if (event.getDragboard().hasFiles()) {
				File file = event.getDragboard().getFiles().get(0);
				if (file.getName().toLowerCase().endsWith(".pdf")) {
					isUploaded = true;
					lastUploadedFile = file;
					showUploadSuccessView();
				}
			}
			event.setDropCompleted(true);
			event.consume();
		});

		view.getChildren().addAll(title, desc, uploadBox);
		contentArea.getChildren().setAll(view);
	}

	private void showUploadSuccessView() {
		resetButtonStyles();
		btnUpload.getStyleClass().add("active");

		VBox view = new VBox(10);
		Label title = new Label("Resume Uploaded Successfully");
		title.getStyleClass().add("view-title");
		Label desc = new Label("Your resume is ready for ATS structural analysis.");
		desc.getStyleClass().add("view-desc");

		VBox successBox = new VBox(10);
		successBox.getStyleClass().add("success-box");
		Label status = new Label("Upload Complete");
		status.getStyleClass().add("success-title");

		HBox fileRow = new HBox();
		fileRow.getStyleClass().add("file-row");
		Label fileName = new Label(lastUploadedFile != null ? lastUploadedFile.getName() : "Resume_File.pdf");
		fileName.getStyleClass().add("file-name-text");
		Label badge = new Label("VERIFIED");
		badge.getStyleClass().add("badge-green");
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		fileRow.getChildren().addAll(fileName, spacer, badge);

		successBox.getChildren().addAll(status, fileRow);
		view.getChildren().addAll(title, desc, successBox);
		contentArea.getChildren().setAll(view);
	}

	private void showStructureView() {
		resetButtonStyles();
		btnStructure.getStyleClass().add("active");

		VBox view = new VBox(15);
		Label title = new Label("Structural Analysis");
		title.getStyleClass().add("view-title");
		Label desc = new Label("The ATS engine detected several formatting risks.");
		desc.getStyleClass().add("view-desc");

		VBox terminal = new VBox(5);
		terminal.getStyleClass().add("terminal");
		terminal.getChildren().addAll(new Label("> Initializing ATS parser..."), new Label("> Extracting PDF text..."),
				new Label("> Detecting technical sections..."), new Label("> Structural analysis complete."));
		StackPane progressBar = new StackPane();
		progressBar.getStyleClass().add("progress-track");
		Region fill = new Region();
		fill.getStyleClass().add("progress-fill-blue");
		progressBar.getChildren().add(fill);
		StackPane.setAlignment(fill, Pos.CENTER_LEFT);
		terminal.getChildren().add(progressBar);

		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(15);

		if (lastUploadedFile != null) {
			String extractedText = extractor.extractText(lastUploadedFile);

			
			if (extractedText.equals("ERROR_EMPTY_IMAGE")) {
				VBox errorPanel = createCard(" Parsing Failed: Scanned Image Detected",
						"DevFilter AI could not extract plain text from this document. The file appears to be a flat image or a physical scan. Please upload a machine-readable document exported directly from text editors like MS Word or Google Docs.",
						"red-card");
				errorPanel.setPadding(new Insets(15));
				view.getChildren().addAll(title, desc, terminal, errorPanel);
				contentArea.getChildren().setAll(view);
				return; 
			} else if (extractedText.startsWith("ERROR_")) {
				VBox errorPanel = createCard(" Parsing Exception",
						"An error occurred while accessing or decrypting the uploaded resume. Ensure the PDF file is not password protected.",
						"red-card");
				view.getChildren().addAll(title, desc, terminal, errorPanel);
				contentArea.getChildren().setAll(view);
				return;
			}

			
			Map<String, Double> pageRiskCheck = extractor.hasExperuence(lastUploadedFile);
			boolean hasExperienceKeyword = extractedText.toLowerCase().contains("experience");

			
			int actualPages = 0;
			try (org.apache.pdfbox.pdmodel.PDDocument doc = org.apache.pdfbox.pdmodel.PDDocument
					.load(lastUploadedFile)) {
				actualPages = doc.getNumberOfPages();
			} catch (java.io.IOException e) {
				System.err.println("Error reading page count in UI: " + e.getMessage());
			}

			VBox pageCard;
		
			if (pageRiskCheck.getOrDefault("NumberOfPages", 0.0) == 1.0) {
				pageCard = createCard("Page Count: High Risk (" + actualPages + " Pages)", hasExperienceKeyword
						? "Critical formatting risk. Since 'Experience' was detected, your CV must not exceed 2 pages. It is currently "
								+ actualPages + " pages."
						: "Critical formatting risk. With no clear work experience detected, your CV strictly needs to be a single page. It is currently "
								+ actualPages + " pages.",
						"red-card");
			} else {
				
				pageCard = createCard(
						"Page Count: Perfect (" + actualPages + " Page" + (actualPages > 1 ? "s" : "") + ")",
						hasExperienceKeyword
								? "Excellent. A 2-page (or less) layout is well-optimized for an applicant with work experience."
								: "Excellent. A clean, single-page layout is perfectly optimized for a candidate starting out.",
						"green-card");
			}

			
			int col = 0, row = 0;
			grid.add(pageCard, col, row);
			col++;
			if (col > 1) {
				col = 0;
				row++;
			}

			
			Map<String, Double> checks = extractor.isCorrectStucture(extractedText);
			String[] order = { "skills", "education", "projects", "experience", "github", "linkedin", "profile" };

			for (String key : order) {
				if (checks.containsKey(key)) {
					grid.add(createDynamicCard(key, checks.get(key)), col, row);
					col++;
					if (col > 1) {
						col = 0;
						row++;
					}
				}
			}
		}

		view.getChildren().addAll(title, desc, terminal, grid);
		contentArea.getChildren().setAll(view);
	}

	private VBox createDynamicCard(String key, Double value) {
		String name = key.substring(0, 1).toUpperCase() + key.substring(1);
		boolean isFound = (value != null && value >= 1.0);

		String cardTitle = isFound ? name + " Found" : name + " Not Found";
		String cardBody = isFound ? "Valid " + name + " section detected successfully."
				: "No clear " + name + " section was detected.";

		String styleClass;
		if (isFound) {
			styleClass = "green-card";
		} else {
			styleClass = (key.equals("github") || key.equals("experience")) ? "red-card" : "orange-card";
		}

		return createCard(cardTitle, cardBody, styleClass);
	}

	private void showScoreView(String finalStatus, String recommendation, String knnLabel, int clusterCount) {

	    resetButtonStyles();
	    btnScore.getStyleClass().add("active");

	    VBox view = new VBox(20);
	    view.setPadding(new Insets(20));

	    Label title = new Label("CV Analysis");
	    title.getStyleClass().add("view-title");

	    HBox layout = new HBox(30);
	    layout.setAlignment(Pos.CENTER_LEFT);



	    StackPane circleStack = new StackPane();

	    Circle bgCircle = new Circle(75, Color.web("#1e293b"));

	    boolean lowQuality = finalStatus.equalsIgnoreCase("LOW QUALITY CV");

	    Color statusColor = lowQuality
	            ? Color.web("#ef4444")
	            : Color.web("#22c55e");

	    Arc scoreArc = new Arc(
	            0,
	            0,
	            75,
	            75,
	            90,
	            lowQuality ? -90 : -270
	    );

	    scoreArc.setType(ArcType.ROUND);
	    scoreArc.setFill(statusColor);

	    Circle innerCircle = new Circle(60, Color.web("#0b1220"));

	    Label statusLabel = new Label(
	            lowQuality ? "LOW" : "HIGH"
	    );

	    statusLabel.setStyle(
	            "-fx-font-size: 24px;" +
	            "-fx-font-weight: 800;" +
	            "-fx-text-fill: " +
	            (lowQuality ? "#ef4444;" : "#22c55e;")
	    );

	    Label cvLabel = new Label("CV QUALITY");

	    cvLabel.setStyle(
	            "-fx-font-size: 10px;" +
	            "-fx-text-fill: #94a3b8;"
	    );

	    VBox scoreText = new VBox(2, statusLabel, cvLabel);
	    scoreText.setAlignment(Pos.CENTER);

	    circleStack.getChildren().addAll(
	            bgCircle,
	            scoreArc,
	            innerCircle,
	            scoreText
	    );


	    VBox stats = new VBox(12);

	    Label classificationLabel = new Label(
	            "CLASSIFICATION: " + knnLabel.toUpperCase()
	    );

	    classificationLabel.setStyle(
	            "-fx-text-fill: " +
	            (lowQuality ? "#ef4444;" : "#22c55e;") +
	            "-fx-font-weight: bold;" +
	            "-fx-font-size: 12px;"
	    );

	    Label qualityLabel = new Label(finalStatus);

	    qualityLabel.setStyle(
	            "-fx-text-fill: #f1f5f9;" +
	            "-fx-font-size: 18px;" +
	            "-fx-font-weight: bold;"
	    );

	    Label similarLabel = new Label(
	            "Similar CV profiles: " + clusterCount
	    );

	    similarLabel.setStyle(
	            "-fx-text-fill: #94a3b8;" +
	            "-fx-font-size: 13px;"
	    );

	    Label recommendationLabel = new Label(
	            recommendation
	    );

	    recommendationLabel.setWrapText(true);
	    recommendationLabel.setMaxWidth(350);

	    recommendationLabel.setStyle(
	            "-fx-text-fill: #cbd5e1;" +
	            "-fx-font-size: 13px;"
	    );

	    stats.getChildren().addAll(
	            classificationLabel,
	            qualityLabel,
	            similarLabel,
	            recommendationLabel
	    );

	    layout.getChildren().addAll(
	            circleStack,
	            stats
	    );

	    view.getChildren().addAll(
	            title,
	            layout
	    );

	    contentArea.getChildren().setAll(view);
	}
	private void showRecommendationsView() {
		resetButtonStyles();
		btnRecommendations.getStyleClass().add("active");
		VBox view = new VBox(15);
		Label title = new Label("Optimization Recommendations");
		title.getStyleClass().add("view-title");
		VBox list = new VBox(6);
		String[] recs = { "Add a GitHub portfolio.", "Replace weak academic wording.", "Remove high school info.",
				"Add clearer language categories." };
		for (String r : recs) {
			Label item = new Label(r);
			item.getStyleClass().add("recommendation-item");
			list.getChildren().add(item);
		}
		VBox blueprint = new VBox(4);
		blueprint.getStyleClass().add("terminal");
		Label layoutTitle = new Label("Recommended Resume Layout");
		layoutTitle.setStyle(
				"-fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-font-family: 'Monospaced'; -fx-font-size: 11px;");
		blueprint.getChildren().add(layoutTitle);
		String[] ly = { "1. Header", "2. Projects", "3. Education" };
		for (String l : ly) {
			Label lyLabel = new Label(l);
			lyLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-family: 'Monospaced'; -fx-font-size: 10px;");
			blueprint.getChildren().add(lyLabel);
		}
		view.getChildren().addAll(title, list, blueprint);
		contentArea.getChildren().setAll(view);
	}

	private void resetButtonStyles() {
		btnUpload.getStyleClass().remove("active");
		btnStructure.getStyleClass().remove("active");
		btnScore.getStyleClass().remove("active");
		btnRecommendations.getStyleClass().remove("active");
	}

	private VBox createCard(String title, String body, String style) {
		VBox card = new VBox(3, new Label(title), new Label(body));
		card.getStyleClass().addAll("card", style);
		((Label) card.getChildren().get(0))
				.setStyle("-fx-font-weight: bold; -fx-text-fill: #f1f5f9; -fx-font-size: 12px;");
		((Label) card.getChildren().get(1))
				.setStyle("-fx-text-fill: #94a3b8; -fx-wrap-text: true; -fx-font-size: 11px;");
		card.setMinWidth(300);
		return card;
	}

	private VBox createStatBar(String label, String percent, String barStyle, double progress) {
		VBox container = new VBox(3);
		Label nameLabel = new Label(label);
		Region spacer = new Region();
		Label percentLabel = new Label(percent);
		HBox labels = new HBox(nameLabel, spacer, percentLabel);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		nameLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 11px;");
		percentLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 11px;");
		StackPane track = new StackPane();
		track.getStyleClass().add("progress-track-small");
		Region fill = new Region();
		fill.getStyleClass().add(barStyle);
		fill.setPrefWidth(250 * progress);
		fill.setMaxWidth(250 * progress);
		track.getChildren().add(fill);
		StackPane.setAlignment(fill, Pos.CENTER_LEFT);
		container.getChildren().addAll(labels, track);
		return container;
	}
}